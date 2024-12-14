package cat.aubrey.pvptils.client.modules;

import com.sun.jna.Native;
import com.sun.jna.Platform;
import com.sun.jna.platform.win32.Kernel32;
import com.sun.jna.platform.win32.Tlhelp32;
import com.sun.jna.platform.win32.WinNT;
import com.sun.jna.platform.win32.WinDef.*;
import com.sun.jna.platform.win32.User32;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import cat.aubrey.pvptils.client.Pvptils;

public class Spotify {
    private static boolean enabled = false;
    private static boolean priorityEnabled = true;
    private static boolean keybindsEnabled = true;
    private static Thread priorityThread;
    private static volatile boolean running = false;

    public interface MyUser32 extends User32 {
        MyUser32 INSTANCE = Native.load("user32", MyUser32.class);
        LRESULT SendMessageA(HWND hWnd, int msg, WPARAM wParam, LPARAM lParam);
    }

    private static final int WM_APPCOMMAND = 0x0319;
    private static final int APPCOMMAND_MEDIA_NEXTTRACK = 11;
    private static final int APPCOMMAND_MEDIA_PREVIOUSTRACK = 12;
    private static final int APPCOMMAND_MEDIA_PLAY_PAUSE = 14;
    private static KeyBinding nextTrackKey;
    private static KeyBinding previousTrackKey;
    private static KeyBinding playPauseKey;

    public static void initializeKeybinds() {
        nextTrackKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvptils.spotify_next",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_BRACKET,
                "category.pvptils.spotify"
        ));

        previousTrackKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvptils.spotify_previous",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_BRACKET,
                "category.pvptils.spotify"
        ));

        playPauseKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.pvptils.spotify_playpause",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_BACKSLASH,
                "category.pvptils.spotify"
        ));
    }

    public static void handleKeybinds() {
        if (!enabled || !keybindsEnabled) return;

        if (nextTrackKey.wasPressed()) {
            sendMediaCommand(APPCOMMAND_MEDIA_NEXTTRACK);
            Pvptils.LOGGER.debug("Spotify: Next Track");
        }
        if (previousTrackKey.wasPressed()) {
            sendMediaCommand(APPCOMMAND_MEDIA_PREVIOUSTRACK);
            Pvptils.LOGGER.debug("Spotify: Previous Track");
        }
        if (playPauseKey.wasPressed()) {
            sendMediaCommand(APPCOMMAND_MEDIA_PLAY_PAUSE);
            Pvptils.LOGGER.debug("Spotify: Play/Pause");
        }
    }

    private static void sendMediaCommand(int command) {
        try {
            if (Platform.isWindows()) {
                sendWindowsMediaCommand(command);
            } else if (Platform.isLinux()) {
                sendLinuxMediaCommand(command);
            } else if (Platform.isMac()) {
                sendMacMediaCommand(command);
            }
        } catch (Exception e) {
            Pvptils.LOGGER.error("Failed to send media command", e);
        }
    }

    private static void sendWindowsMediaCommand(int command) {
        HWND hwnd = MyUser32.INSTANCE.GetForegroundWindow();
        if (hwnd != null) {
            WPARAM wParam = new WPARAM(0);
            LPARAM lParam = new LPARAM(command << 16);
            MyUser32.INSTANCE.SendMessageA(hwnd, WM_APPCOMMAND, wParam, lParam);
        }
    }

    private static void sendLinuxMediaCommand(int command) {
        try {
            String cmd = switch (command) {
                case APPCOMMAND_MEDIA_NEXTTRACK -> "dbus-send --print-reply --dest=org.mpris.MediaPlayer2.spotify /org/mpris/MediaPlayer2 org.mpris.MediaPlayer2.Player.Next";
                case APPCOMMAND_MEDIA_PREVIOUSTRACK -> "dbus-send --print-reply --dest=org.mpris.MediaPlayer2.spotify /org/mpris/MediaPlayer2 org.mpris.MediaPlayer2.Player.Previous";
                case APPCOMMAND_MEDIA_PLAY_PAUSE -> "dbus-send --print-reply --dest=org.mpris.MediaPlayer2.spotify /org/mpris/MediaPlayer2 org.mpris.MediaPlayer2.Player.PlayPause";
                default -> null;
            };
            if (cmd != null) {
                Runtime.getRuntime().exec(cmd);
            }
        } catch (Exception e) {
            Pvptils.LOGGER.error("Failed to send Linux media command", e);
        }
    }

    private static void sendMacMediaCommand(int command) {
        try {
            String cmd = switch (command) {
                case APPCOMMAND_MEDIA_NEXTTRACK -> "osascript -e 'tell application \"Spotify\" to next track'";
                case APPCOMMAND_MEDIA_PREVIOUSTRACK -> "osascript -e 'tell application \"Spotify\" to previous track'";
                case APPCOMMAND_MEDIA_PLAY_PAUSE -> "osascript -e 'tell application \"Spotify\" to playpause'";
                default -> null;
            };
            if (cmd != null) {
                Runtime.getRuntime().exec(new String[]{"bash", "-c", cmd});
            }
        } catch (Exception e) {
            Pvptils.LOGGER.error("Failed to send Mac media command", e);
        }
    }

    private static void prioritizeSpotify() {
        if (!Platform.isWindows()) return;

        Kernel32 kernel32 = Kernel32.INSTANCE;
        WinNT.HANDLE snapshot = kernel32.CreateToolhelp32Snapshot(
                Tlhelp32.TH32CS_SNAPPROCESS,
                new DWORD(0)
        );

        if (snapshot == null) return;

        Tlhelp32.PROCESSENTRY32.ByReference processEntry = new Tlhelp32.PROCESSENTRY32.ByReference();

        try {
            while (kernel32.Process32Next(snapshot, processEntry)) {
                String processName = Native.toString(processEntry.szExeFile);
                if (processName.toLowerCase().contains("spotify")) {
                    setProcessPriority(processEntry.th32ProcessID.intValue());
                    break;
                }
            }
        } finally {
            kernel32.CloseHandle(snapshot);
        }
    }

    private static void setProcessPriority(int pid) {
        if (!Platform.isWindows()) return;

        Kernel32 kernel32 = Kernel32.INSTANCE;
        WinNT.HANDLE processHandle = kernel32.OpenProcess(
                Kernel32.PROCESS_SET_INFORMATION,
                false,
                pid
        );

        if (processHandle != null) {
            try {
                kernel32.SetPriorityClass(processHandle, Kernel32.HIGH_PRIORITY_CLASS);
            } finally {
                kernel32.CloseHandle(processHandle);
            }
        }
    }

    private static void startPriorityThread() {
        if (priorityThread != null && priorityThread.isAlive()) return;

        running = true;
        priorityThread = new Thread(() -> {
            while (running) {
                try {
                    prioritizeSpotify();
                    Thread.sleep(1000);
                } catch (Exception e) {
                    Pvptils.LOGGER.error("Error in Spotify priority thread", e);
                }
            }
        }, "Spotify-Priority-Thread");

        priorityThread.setDaemon(true);
        priorityThread.start();
        Pvptils.LOGGER.debug("Spotify priority thread started");
    }

    private static void stopPriorityThread() {
        running = false;
        if (priorityThread != null) {
            priorityThread.interrupt();
            priorityThread = null;
            Pvptils.LOGGER.debug("Spotify priority thread stopped");
        }
    }

    public static void setEnabled(boolean value) {
        if (value == enabled) return;
        enabled = value;

        if (enabled && priorityEnabled && Platform.isWindows()) {
            startPriorityThread();
        } else {
            stopPriorityThread();
        }

        Pvptils.LOGGER.info("Spotify module {} ", enabled ? "enabled" : "disabled");
    }

    public static void setPriorityEnabled(boolean value) {
        if (value == priorityEnabled) return;
        priorityEnabled = value;

        if (enabled && priorityEnabled && Platform.isWindows()) {
            startPriorityThread();
        } else {
            stopPriorityThread();
        }
    }

    public static void setKeybindsEnabled(boolean value) {
        keybindsEnabled = value;
    }

    public static boolean isEnabled() {
        return enabled;
    }
}