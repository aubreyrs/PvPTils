package cat.aubrey.pvptils.client.config.modules;

import cat.aubrey.pvptils.client.config.base.ConfigModule;
import cat.aubrey.pvptils.client.config.base.ConfigRegistry;
import cat.aubrey.pvptils.client.modules.HitboxDisplay;
import com.google.gson.annotations.Expose;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.Text;
import cat.aubrey.pvptils.client.Pvptils;

import java.io.*;

public class HitboxConfig implements ConfigModule {
    private static final String FILE_NAME = "hitbox.json";

    @Expose private int hitboxColor = 0xFF0000;
    @Expose private boolean showEyeHeight = false;
    @Expose private boolean showLookVector = false;
    @Expose private float hitboxScale = 1.0f;

    @Override
    public void save() {
        try (Writer writer = new FileWriter(new File("config/pvptils", FILE_NAME))) {
            ConfigRegistry.GSON.toJson(this, writer);
        } catch (IOException e) {
            Pvptils.LOGGER.error("Failed to save hitbox config", e);
        }
    }

    @Override
    public void load() {
        File file = new File("config/pvptils", FILE_NAME);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                HitboxConfig loaded = ConfigRegistry.GSON.fromJson(reader, HitboxConfig.class);
                copyFrom(loaded);
                applyToModule();
            } catch (IOException e) {
                Pvptils.LOGGER.error("Failed to load hitbox config", e);
            }
        }
    }

    private void copyFrom(HitboxConfig other) {
        this.hitboxColor = other.hitboxColor;
        this.showEyeHeight = other.showEyeHeight;
        this.showLookVector = other.showLookVector;
        this.hitboxScale = other.hitboxScale;
    }

    private void applyToModule() {
        HitboxDisplay.setHitboxColor(hitboxColor);
        HitboxDisplay.setShowEyeHeight(showEyeHeight);
        HitboxDisplay.setShowLookVector(showLookVector);
        HitboxDisplay.setHitboxScale(hitboxScale);
    }

    @Override
    public ConfigCategory buildCategory(ConfigBuilder builder) {
        return builder.getOrCreateCategory(getCategoryText())
                .addEntry(builder.entryBuilder()
                        .startColorField(Text.literal("Hitbox Color"), hitboxColor)
                        .setDefaultValue(0xFF0000)
                        .setTooltip(Text.literal("Color of entity hitboxes"))
                        .setSaveConsumer(value -> {
                            hitboxColor = value;
                            HitboxDisplay.setHitboxColor(value);
                            save();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startFloatField(Text.literal("Hitbox Scale"), hitboxScale)
                        .setDefaultValue(1.0f)
                        .setMin(0.1f).setMax(2.0f)
                        .setTooltip(Text.literal("Size multiplier for hitbox display"))
                        .setSaveConsumer(value -> {
                            hitboxScale = value;
                            HitboxDisplay.setHitboxScale(value);
                            save();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Show Eye Height"), showEyeHeight)
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("Show entity eye height indicator"))
                        .setSaveConsumer(value -> {
                            showEyeHeight = value;
                            HitboxDisplay.setShowEyeHeight(value);
                            save();
                        }).build())
                .addEntry(builder.entryBuilder()
                        .startBooleanToggle(Text.literal("Show Look Vector"), showLookVector)
                        .setDefaultValue(false)
                        .setTooltip(Text.literal("Show entity look direction indicator"))
                        .setSaveConsumer(value -> {
                            showLookVector = value;
                            HitboxDisplay.setShowLookVector(value);
                            save();
                        }).build());
    }

    @Override
    public Text getCategoryText() {
        return Text.literal("Hitboxes");
    }

    public int getHitboxColor() { return hitboxColor; }
    public boolean shouldShowEyeHeight() { return showEyeHeight; }
    public boolean shouldShowLookVector() { return showLookVector; }
    public float getHitboxScale() { return hitboxScale; }
}