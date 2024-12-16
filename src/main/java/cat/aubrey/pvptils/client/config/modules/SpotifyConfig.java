package cat.aubrey.pvptils.client.config.modules;

import cat.aubrey.pvptils.client.config.base.ConfigModule;
import cat.aubrey.pvptils.client.config.base.ConfigRegistry;
import cat.aubrey.pvptils.client.modules.Spotify;
import com.google.gson.annotations.Expose;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.Text;
import cat.aubrey.pvptils.client.Pvptils;
import net.minecraft.client.gui.screen.Screen;

import java.io.*;

public class SpotifyConfig implements ConfigModule {
    private static final String FILE_NAME = "spotify.json";

    @Expose private boolean enabled = false;
    @Expose private boolean priority = true;
    @Expose private boolean keybindsEnabled = true;
    @Expose private boolean hudEnabled = true;
    @Expose private int hudColor = 0xFFFFFF;
    @Expose private int hudPosition = 0;

    @Override
    public void save() {
        try (Writer writer = new FileWriter(new File("config/pvptils", FILE_NAME))) {
            ConfigRegistry.GSON.toJson(this, writer);
        } catch (IOException e) {
            Pvptils.LOGGER.error("Failed to save spotify config", e);
        }
    }

    @Override
    public void load() {
        File file = new File("config/pvptils", FILE_NAME);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                SpotifyConfig loaded = ConfigRegistry.GSON.fromJson(reader, SpotifyConfig.class);
                copyFrom(loaded);
                applyToModule();
            } catch (IOException e) {
                Pvptils.LOGGER.error("Failed to load spotify config", e);
            }
        }
    }

    private void copyFrom(SpotifyConfig other) {
        this.enabled = other.enabled;
        this.priority = other.priority;
        this.keybindsEnabled = other.keybindsEnabled;
        this.hudEnabled = other.hudEnabled;
        this.hudColor = other.hudColor;
        this.hudPosition = other.hudPosition;
    }

    private void applyToModule() {
        Spotify.setEnabled(enabled);
        Spotify.setPriorityEnabled(priority);
        Spotify.setKeybindsEnabled(keybindsEnabled);
    }

    @Override
    public ConfigCategory buildCategory(ConfigBuilder builder) {
        return builder.getOrCreateCategory(getCategoryText())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Enable Spotify Module"), enabled)
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("Enable Spotify integration"))
                        .setSaveConsumer(value -> {
                            enabled = value;
                            Spotify.setEnabled(value);
                            save();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Spotify Priority"), priority)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Prioritize Spotify audio when playing"))
                        .setSaveConsumer(value -> {
                            priority = value;
                            Spotify.setPriorityEnabled(value);
                            save();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Enable Keybinds"), keybindsEnabled)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Enable media control keybinds"))
                        .setSaveConsumer(value -> {
                            keybindsEnabled = value;
                            Spotify.setKeybindsEnabled(value);
                            save();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Show HUD"), hudEnabled)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Show currently playing song in game"))
                        .setSaveConsumer(value -> {
                            hudEnabled = value;
                            save();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startColorField(Text.literal("HUD Color"), hudColor)
                        .setDefaultValue(0xFFFFFF)
                        .setTooltip(Text.literal("Color of the song display"))
                        .setSaveConsumer(value -> {
                            hudColor = value;
                            save();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Text.literal("HUD Position"),
                                HudPosition.class,
                                HudPosition.values()[hudPosition])
                        .setDefaultValue(HudPosition.BOTTOM_LEFT)
                        .setTooltip(Text.literal("Position of the song display on screen"))
                        .setSaveConsumer(value -> {
                            hudPosition = value.ordinal();
                            save();
                        }).build());
    }

    @Override
    public Text getCategoryText() {
        return Text.literal("Spotify");
    }

    public boolean isEnabled() { return enabled; }
    public boolean isPriorityEnabled() { return priority; }
    public boolean isKeybindsEnabled() { return keybindsEnabled; }
    public boolean isHudEnabled() { return hudEnabled; }
    public int getHudColor() { return hudColor; }
    public int getHudPosition() { return hudPosition; }

    public enum HudPosition {
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        TOP_RIGHT
    }
}