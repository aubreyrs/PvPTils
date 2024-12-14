package cat.aubrey.pvptils.client.modules;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.User32;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.DWORD;
import com.sun.jna.platform.win32.WinNT.HANDLE;
import com.sun.jna.ptr.IntByReference;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.text.Text;
import cat.aubrey.pvptils.client.Config;
import cat.aubrey.pvptils.client.Config.HudPosition;
import cat.aubrey.pvptils.client.Pvptils;
import net.minecraft.client.util.Window;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class SpotifyHud {
    private static final MinecraftClient client = MinecraftClient.getInstance();
    private static String currentSong = "";
    private static long lastUpdate = 0;
    private static final int UPDATE_INTERVAL = 1000;
    private static final int PADDING = 5;
    private static final int TH32CS_SNAPPROCESS = 0x00000002;
    private static final String LINUX_METADATA_COMMAND =
            "dbus-send --print-reply --dest=org.mpris.MediaPlayer2.spotify /org/mpris/MediaPlayer2 " +
                    "org.freedesktop.DBus.Properties.Get string:org.mpris.MediaPlayer2.Player string:Metadata";

    public interface SpotifyUser32 extends User32 {
        SpotifyUser32 INSTANCE = Native.load("user32", SpotifyUser32.class);
        int GetWindowTextA(HWND hWnd, byte[] lpString, int nMaxCount);
        int GetWindowTextLengthA(HWND hWnd);
        int GetClassNameA(HWND hWnd, byte[] lpClassName, int nMaxCount);
        boolean EnumWindows(User32.WNDENUMPROC lpEnumFunc, Pointer arg);
        int GetWindowThreadProcessId(HWND hWnd, IntByReference lpdwProcessId);
    }

    public static void updateCurrentSong() {
        if (!Spotify.isEnabled() || !Config.get().isSpotifyHudEnabled() || Platform.isMac()) {
            return;
        }

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate < UPDATE_INTERVAL) return;
        lastUpdate = currentTime;

        try {
            if (Platform.isWindows()) {
                updateWindowsSong();
            } else if (Platform.isLinux()) {
                updateLinuxSong();
            }
        } catch (Exception e) {
            Pvptils.LOGGER.error("Error updating song", e);
            currentSong = "";
        }
    }

    private static void updateWindowsSong() {
        HWND spotifyWindow = findSpotifyWindow();
        if (spotifyWindow != null) {
            int length = SpotifyUser32.INSTANCE.GetWindowTextLengthA(spotifyWindow) + 1;
            byte[] buffer = new byte[length];
            SpotifyUser32.INSTANCE.GetWindowTextA(spotifyWindow, buffer, length);
            String windowTitle = Native.toString(buffer);

            if (isUnwantedTitle(windowTitle)) {
                currentSong = "";
                return;
            }

            if (!windowTitle.equals("Spotify") && !windowTitle.isEmpty()) {
                currentSong = windowTitle.replace(" - Spotify", "");
            }
        }
    }

    private static void updateLinuxSong() {
        try {
            Process process = Runtime.getRuntime().exec(LINUX_METADATA_COMMAND);
            if (process.waitFor(2, TimeUnit.SECONDS)) {
                try (BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()))) {
                    StringBuilder metadata = new StringBuilder();
                    String line;
                    String artist = "";
                    String title = "";

                    while ((line = reader.readLine()) != null) {
                        if (line.contains("xesam:artist")) {
                            line = reader.readLine();
                            if (line != null) {
                                artist = line.trim().replaceAll("^string \"(.*)\"$", "$1");
                            }
                        } else if (line.contains("xesam:title")) {
                            line = reader.readLine();
                            if (line != null) {
                                title = line.trim().replaceAll("^string \"(.*)\"$", "$1");
                            }
                        }
                    }

                    if (!artist.isEmpty() && !title.isEmpty()) {
                        currentSong = artist + " - " + title;
                    } else if (!title.isEmpty()) {
                        currentSong = title;
                    } else {
                        currentSong = "";
                    }
                }
            } else {
                process.destroy();
                currentSong = "";
            }
        } catch (IOException | InterruptedException e) {
            Pvptils.LOGGER.error("Error getting Linux Spotify metadata", e);
            currentSong = "";
        }
    }

    private static boolean isUnwantedTitle(String title) {
        String[] unwantedTitles = {"Spotify Free", "Advertisement", "Spotify Premium", "Spotify"};
        for (String unwanted : unwantedTitles) {
            if (title.contains(unwanted)) {
                return true;
            }
        }
        return false;
    }

    private static HWND findSpotifyWindow() {
        if (!Platform.isWindows()) return null;

        final HWND[] result = new HWND[1];

        User32.WNDENUMPROC callback = (HWND hwnd, Pointer data) -> {
            byte[] classNameBuffer = new byte[256];
            SpotifyUser32.INSTANCE.GetClassNameA(hwnd, classNameBuffer, 256);
            String className = Native.toString(classNameBuffer);

            if (className.equals("Chrome_WidgetWin_1")) {
                IntByReference processIdRef = new IntByReference(0);
                SpotifyUser32.INSTANCE.GetWindowThreadProcessId(hwnd, processIdRef);
                int processId = processIdRef.getValue();

                if (isSpotifyProcess(processId)) {
                    result[0] = hwnd;
                    return false;
                }
            }
            return true;
        };

        try {
            SpotifyUser32.INSTANCE.EnumWindows(callback, null);
        } catch (Exception e) {
            Pvptils.LOGGER.error("Error enumerating windows", e);
        }

        return result[0];
    }

    private static boolean isSpotifyProcess(int processId) {
        if (!Platform.isWindows()) return false;

        try {
            Tlhelp32.PROCESSENTRY32 processEntry = new Tlhelp32.PROCESSENTRY32();
            HANDLE snapshot = Kernel32.INSTANCE.CreateToolhelp32Snapshot(
                    new DWORD(TH32CS_SNAPPROCESS), new DWORD(0));

            try {
                if (Kernel32.INSTANCE.Process32First(snapshot, processEntry)) {
                    do {
                        if (processEntry.th32ProcessID.intValue() == processId) {
                            String processName = Native.toString(processEntry.szExeFile);
                            return processName.equalsIgnoreCase("Spotify.exe");
                        }
                    } while (Kernel32.INSTANCE.Process32Next(snapshot, processEntry));
                }
            } finally {
                Kernel32.INSTANCE.CloseHandle(snapshot);
            }
        } catch (Exception e) {
            Pvptils.LOGGER.error("Error checking process name", e);
        }
        return false;
    }

    public static void render(DrawContext context) {
        if (!Spotify.isEnabled() || !Config.get().isSpotifyHudEnabled() ||
                currentSong == null || currentSong.isEmpty() || Platform.isMac()) {
            return;
        }

        Window window = client.getWindow();
        int screenWidth = window.getScaledWidth();
        int screenHeight = window.getScaledHeight();

        Text text = Text.literal("♫ " + currentSong);
        int textWidth = client.textRenderer.getWidth(text);
        int textHeight = client.textRenderer.fontHeight;

        int x, y;
        HudPosition position = HudPosition.values()[Config.get().getSpotifyHudPosition()];

        switch (position) {
            case TOP_LEFT:
                x = PADDING;
                y = PADDING;
                break;
            case TOP_RIGHT:
                x = screenWidth - textWidth - PADDING;
                y = PADDING;
                break;
            case BOTTOM_RIGHT:
                x = screenWidth - textWidth - PADDING;
                y = screenHeight - textHeight - PADDING;
                break;
            case BOTTOM_LEFT:
            default:
                x = PADDING;
                y = screenHeight - textHeight - PADDING;
                break;
        }

        context.fill(
                x - 2,
                y - 2,
                x + textWidth + 2,
                y + textHeight + 2,
                0x80000000
        );

        context.drawTextWithShadow(
                client.textRenderer,
                text,
                x,
                y,
                Config.get().getSpotifyHudColor()
        );
    }
}