package cat.aubrey.pvptils.client;

import cat.aubrey.pvptils.client.config.ConfigData;
import cat.aubrey.pvptils.client.modules.HitboxDisplay;
import cat.aubrey.pvptils.client.modules.ParticleEffects;
import cat.aubrey.pvptils.client.modules.Spotify;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    private static Config INSTANCE;
    private static final String CONFIG_FILE = "pvptils.json";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final ConfigData data;

    private Config() {
        this.data = new ConfigData();
        loadConfig();
    }

    public static Config get() {
        if (INSTANCE == null) INSTANCE = new Config();
        return INSTANCE;
    }

    public boolean isLightningKillEffectEnabled() { return data.isLightningKillEffectEnabled(); }
    public boolean isSpotifyEnabled() { return data.isSpotifyEnabled(); }
    public boolean isSpotifyPriorityEnabled() { return data.isSpotifyPriorityEnabled(); }
    public boolean isSpotifyKeybindsEnabled() { return data.isSpotifyKeybindsEnabled(); }
    public boolean isSpotifyHudEnabled() { return data.isSpotifyHudEnabled(); }
    public int getSpotifyHudColor() { return data.getSpotifyHudColor(); }
    public int getSpotifyHudPosition() { return data.getSpotifyHudPosition(); }
    public int getHitboxColor() { return data.getHitboxColor(); }
    public float getHitboxScale() { return data.getHitboxScale(); }
    public boolean shouldShowEyeHeight() { return data.shouldShowEyeHeight(); }
    public boolean shouldShowLookVector() { return data.shouldShowLookVector(); }

    public Screen buildScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Text.literal("PvPtils Configuration"));

        builder.getOrCreateCategory(Text.literal("General"))
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Lightning Kill Effect"), data.isLightningKillEffectEnabled())
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Shows a lightning effect when an entity dies (client-side)"))
                        .setSaveConsumer(newValue -> {
                            data.setLightningKillEffect(newValue);
                            saveConfig();
                        }).build());

        builder.getOrCreateCategory(Text.literal("Particles"))
                .addEntry(builder.entryBuilder()
                        .startFloatField(Text.literal("Critical Particle Size"), data.getCriticalParticleScale())
                        .setDefaultValue(1.0f)
                        .setMin(0.1f).setMax(3.0f)
                        .setTooltip(Text.literal("Size multiplier for critical hit particles"))
                        .setSaveConsumer(newValue -> {
                            data.setCriticalParticleScale(newValue);
                            ParticleEffects.setCriticalParticleScale(newValue);
                            saveConfig();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startFloatField(Text.literal("Sharpness Particle Size"), data.getSharpnessParticleScale())
                        .setDefaultValue(1.0f)
                        .setMin(0.1f).setMax(3.0f)
                        .setTooltip(Text.literal("Size multiplier for sharpness/enchantment particles"))
                        .setSaveConsumer(newValue -> {
                            data.setSharpnessParticleScale(newValue);
                            ParticleEffects.setSharpnessParticleScale(newValue);
                            saveConfig();
                        }).build());

        builder.getOrCreateCategory(Text.literal("Hitboxes"))
                .addEntry(builder.entryBuilder()
                        .startColorField(Text.literal("Hitbox Color"), data.getHitboxColor())
                        .setDefaultValue(0xFF0000)
                        .setTooltip(Text.literal("Color of entity hitboxes"))
                        .setSaveConsumer(newValue -> {
                            data.setHitboxColor(newValue);
                            HitboxDisplay.setHitboxColor(newValue);
                            saveConfig();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startFloatField(Text.literal("Hitbox Scale"), data.getHitboxScale())
                        .setDefaultValue(1.0f)
                        .setMin(0.1f).setMax(2.0f)
                        .setTooltip(Text.literal("Size multiplier for hitbox display"))
                        .setSaveConsumer(newValue -> {
                            data.setHitboxScale(newValue);
                            HitboxDisplay.setHitboxScale(newValue);
                            saveConfig();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Show Eye Height"), data.shouldShowEyeHeight())
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("Show entity eye height indicator"))
                        .setSaveConsumer(newValue -> {
                            data.setShowEyeHeight(newValue);
                            HitboxDisplay.setShowEyeHeight(newValue);
                            saveConfig();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Show Look Vector"), data.shouldShowLookVector())
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("Show entity look direction indicator"))
                        .setSaveConsumer(newValue -> {
                            data.setShowLookVector(newValue);
                            HitboxDisplay.setShowLookVector(newValue);
                            saveConfig();
                        }).build());

        builder.getOrCreateCategory(Text.literal("Spotify"))
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Enable Spotify Module"), data.isSpotifyEnabled())
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("Enable Spotify integration"))
                        .setSaveConsumer(newValue -> {
                            data.setSpotifyEnabled(newValue);
                            Spotify.setEnabled(newValue);
                            saveConfig();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Spotify Priority"), data.isSpotifyPriorityEnabled())
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Prioritize Spotify audio when playing"))
                        .setSaveConsumer(newValue -> {
                            data.setSpotifyPriority(newValue);
                            Spotify.setPriorityEnabled(newValue);
                            saveConfig();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Enable Keybinds"), data.isSpotifyKeybindsEnabled())
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Enable media control keybinds"))
                        .setSaveConsumer(newValue -> {
                            data.setSpotifyKeybindsEnabled(newValue);
                            Spotify.setKeybindsEnabled(newValue);
                            saveConfig();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Show HUD"), data.isSpotifyHudEnabled())
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Show currently playing song in game"))
                        .setSaveConsumer(newValue -> {
                            data.setSpotifyHudEnabled(newValue);
                            saveConfig();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startColorField(Text.literal("HUD Color"), data.getSpotifyHudColor())
                        .setDefaultValue(0xFFFFFF)
                        .setTooltip(Text.literal("Color of the song display"))
                        .setSaveConsumer(newValue -> {
                            data.setSpotifyHudColor(newValue);
                            saveConfig();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startEnumSelector(Text.literal("HUD Position"), HudPosition.class, HudPosition.values()[data.getSpotifyHudPosition()])
                        .setDefaultValue(HudPosition.BOTTOM_LEFT)
                        .setTooltip(Text.literal("Position of the song display on screen"))
                        .setSaveConsumer(newValue -> {
                            data.setSpotifyHudPosition(newValue.ordinal());
                            saveConfig();
                        }).build());

        return builder.build();
    }

    private void loadConfig() {
        try {
            File configDir = new File("config");
            if (!configDir.exists()) {
                configDir.mkdir();
            }

            File configFile = new File(configDir, CONFIG_FILE);
            if (configFile.exists()) {
                try (FileReader reader = new FileReader(configFile)) {
                    ConfigData loadedData = GSON.fromJson(reader, ConfigData.class);
                    data.setLightningKillEffect(loadedData.isLightningKillEffectEnabled());
                    data.setHitboxColor(loadedData.getHitboxColor());
                    data.setShowEyeHeight(loadedData.shouldShowEyeHeight());
                    data.setShowLookVector(loadedData.shouldShowLookVector());
                    data.setHitboxScale(loadedData.getHitboxScale());
                    data.setSpotifyEnabled(loadedData.isSpotifyEnabled());
                    data.setSpotifyPriority(loadedData.isSpotifyPriorityEnabled());
                    data.setSpotifyKeybindsEnabled(loadedData.isSpotifyKeybindsEnabled());
                    data.setSpotifyHudEnabled(loadedData.isSpotifyHudEnabled());
                    data.setSpotifyHudColor(loadedData.getSpotifyHudColor());
                    data.setSpotifyHudPosition(loadedData.getSpotifyHudPosition());
                    HitboxDisplay.setHitboxColor(data.getHitboxColor());
                    HitboxDisplay.setHitboxScale(data.getHitboxScale());
                    HitboxDisplay.setShowEyeHeight(data.shouldShowEyeHeight());
                    HitboxDisplay.setShowLookVector(data.shouldShowLookVector());
                    Spotify.setEnabled(data.isSpotifyEnabled());
                    Spotify.setPriorityEnabled(data.isSpotifyPriorityEnabled());
                    Spotify.setKeybindsEnabled(data.isSpotifyKeybindsEnabled());
                    ParticleEffects.setCriticalParticleScale(loadedData.getCriticalParticleScale());
                    ParticleEffects.setSharpnessParticleScale(loadedData.getSharpnessParticleScale());
                }
            }
        } catch (IOException e) {
            Pvptils.LOGGER.error("Failed to load config", e);
        }
    }

    private void saveConfig() {
        try {
            File configDir = new File("config");
            if (!configDir.exists()) {
                configDir.mkdir();
            }

            try (FileWriter writer = new FileWriter(new File(configDir, CONFIG_FILE))) {
                GSON.toJson(data, writer);
            }
        } catch (IOException e) {
            Pvptils.LOGGER.error("Failed to save config", e);
        }
    }

    public enum HudPosition {
        BOTTOM_LEFT,
        BOTTOM_RIGHT,
        TOP_LEFT,
        TOP_RIGHT
    }
}