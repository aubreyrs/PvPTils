package cat.aubrey.pvptils.client.config.modules;

import cat.aubrey.pvptils.client.config.base.ConfigModule;
import cat.aubrey.pvptils.client.config.base.ConfigRegistry;
import cat.aubrey.pvptils.client.modules.ParticleEffects;
import com.google.gson.annotations.Expose;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import net.minecraft.text.Text;
import cat.aubrey.pvptils.client.Pvptils;

import java.io.*;

public class ParticleConfig implements ConfigModule {
    private static final String FILE_NAME = "particles.json";

    @Expose private float criticalParticleScale = 1.0f;
    @Expose private float sharpnessParticleScale = 1.0f;
    @Expose private float criticalParticleMultiplier = 1.0f;
    @Expose private float sharpnessParticleMultiplier = 1.0f;
    @Expose private int criticalParticleColor = 0xFFFFFF;
    @Expose private int enchantedHitColor = 0x8B00FF;
    @Expose private boolean useCustomCriticalColor = false;
    @Expose private boolean useCustomEnchantedColor = false;
    @Expose private boolean useRainbowCriticalColor = false;
    @Expose private boolean useRainbowEnchantedColor = false;
    @Expose private float rainbowSpeed = 1.0f;
    @Expose private boolean alwaysShowEnchantedHit = false;

    @Override
    public void save() {
        try (Writer writer = new FileWriter(new File("config/pvptils", FILE_NAME))) {
            ConfigRegistry.GSON.toJson(this, writer);
        } catch (IOException e) {
            Pvptils.LOGGER.error("Failed to save particle config", e);
        }
    }

    @Override
    public void load() {
        File file = new File("config/pvptils", FILE_NAME);
        if (file.exists()) {
            try (Reader reader = new FileReader(file)) {
                ParticleConfig loaded = ConfigRegistry.GSON.fromJson(reader, ParticleConfig.class);
                copyFrom(loaded);
                applyToModule();
            } catch (IOException e) {
                Pvptils.LOGGER.error("Failed to load particle config", e);
            }
        } else {
            applyToModule();
        }
    }

    private void copyFrom(ParticleConfig other) {
        this.criticalParticleScale = other.criticalParticleScale;
        this.sharpnessParticleScale = other.sharpnessParticleScale;
        this.criticalParticleMultiplier = other.criticalParticleMultiplier;
        this.sharpnessParticleMultiplier = other.sharpnessParticleMultiplier;
        this.criticalParticleColor = other.criticalParticleColor;
        this.enchantedHitColor = other.enchantedHitColor;
        this.useCustomCriticalColor = other.useCustomCriticalColor;
        this.useCustomEnchantedColor = other.useCustomEnchantedColor;
        this.useRainbowCriticalColor = other.useRainbowCriticalColor;
        this.useRainbowEnchantedColor = other.useRainbowEnchantedColor;
        this.rainbowSpeed = other.rainbowSpeed;
        this.alwaysShowEnchantedHit = other.alwaysShowEnchantedHit;
    }

    private void applyToModule() {
        ParticleEffects.setCriticalParticleScale(criticalParticleScale);
        ParticleEffects.setSharpnessParticleScale(sharpnessParticleScale);
        ParticleEffects.setCriticalParticleMultiplier(criticalParticleMultiplier);
        ParticleEffects.setSharpnessParticleMultiplier(sharpnessParticleMultiplier);
        ParticleEffects.setCriticalParticleColor(criticalParticleColor);
        ParticleEffects.setEnchantedHitColor(enchantedHitColor);
        ParticleEffects.setUseCustomCriticalColor(useCustomCriticalColor);
        ParticleEffects.setUseCustomEnchantedColor(useCustomEnchantedColor);
        ParticleEffects.setUseRainbowCriticalColor(useRainbowCriticalColor);
        ParticleEffects.setUseRainbowEnchantedColor(useRainbowEnchantedColor);
        ParticleEffects.setRainbowSpeed(rainbowSpeed);
        ParticleEffects.setAlwaysShowEnchantedHit(alwaysShowEnchantedHit);
    }

    @Override
    public ConfigCategory buildCategory(ConfigBuilder builder) {
        ConfigCategory category = builder.getOrCreateCategory(getCategoryText());

        category.addEntry(builder.entryBuilder()
                .startFloatField(Text.literal("Critical Particle Size"), criticalParticleScale)
                .setDefaultValue(1.0f)
                .setMin(0.1f).setMax(3.0f)
                .setTooltip(Text.literal("Size multiplier for critical hit particles"))
                .setSaveConsumer(value -> {
                    criticalParticleScale = value;
                    ParticleEffects.setCriticalParticleScale(value);
                    save();
                }).build());

        category.addEntry(builder.entryBuilder()
                .startFloatField(Text.literal("Critical Particle Multiplier"), criticalParticleMultiplier)
                .setDefaultValue(1.0f)
                .setMin(1.0f).setMax(5.0f)
                .setTooltip(Text.literal("Multiplier for critical hit particle count"))
                .setSaveConsumer(value -> {
                    criticalParticleMultiplier = value;
                    ParticleEffects.setCriticalParticleMultiplier(value);
                    save();
                }).build());

        category.addEntry(builder.entryBuilder()
                .startBooleanToggle(Text.literal("Use Custom Critical Color"), useCustomCriticalColor)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Enable custom coloring for critical hit particles"))
                .setSaveConsumer(value -> {
                    useCustomCriticalColor = value;
                    ParticleEffects.setUseCustomCriticalColor(value);
                    save();
                }).build());

        category.addEntry(builder.entryBuilder()
                .startColorField(Text.literal("Critical Hit Particle Color"), criticalParticleColor)
                .setDefaultValue(0xFFFFFF)
                .setTooltip(Text.literal("Color of critical hit particles"))
                .setSaveConsumer(value -> {
                    criticalParticleColor = value;
                    ParticleEffects.setCriticalParticleColor(value);
                    save();
                }).build());

        category.addEntry(builder.entryBuilder()
                .startBooleanToggle(Text.literal("Rainbow Critical Particles"), useRainbowCriticalColor)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Enable rainbow effect for critical hit particles"))
                .setSaveConsumer(value -> {
                    useRainbowCriticalColor = value;
                    ParticleEffects.setUseRainbowCriticalColor(value);
                    save();
                }).build());

        category.addEntry(builder.entryBuilder()
                .startFloatField(Text.literal("Sharpness Particle Size"), sharpnessParticleScale)
                .setDefaultValue(1.0f)
                .setMin(0.1f).setMax(3.0f)
                .setTooltip(Text.literal("Size multiplier for sharpness/enchantment particles"))
                .setSaveConsumer(value -> {
                    sharpnessParticleScale = value;
                    ParticleEffects.setSharpnessParticleScale(value);
                    save();
                }).build());

        category.addEntry(builder.entryBuilder()
                .startFloatField(Text.literal("Sharpness Particle Multiplier"), sharpnessParticleMultiplier)
                .setDefaultValue(1.0f)
                .setMin(1.0f).setMax(5.0f)
                .setTooltip(Text.literal("Multiplier for enchanted hit particle count"))
                .setSaveConsumer(value -> {
                    sharpnessParticleMultiplier = value;
                    ParticleEffects.setSharpnessParticleMultiplier(value);
                    save();
                }).build());

        category.addEntry(builder.entryBuilder()
                .startBooleanToggle(Text.literal("Use Custom Enchanted Color"), useCustomEnchantedColor)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Enable custom coloring for enchanted hit particles"))
                .setSaveConsumer(value -> {
                    useCustomEnchantedColor = value;
                    ParticleEffects.setUseCustomEnchantedColor(value);
                    save();
                }).build());

        category.addEntry(builder.entryBuilder()
                .startColorField(Text.literal("Enchanted Hit Particle Color"), enchantedHitColor)
                .setDefaultValue(0x8B00FF)
                .setTooltip(Text.literal("Color of enchanted hit particles"))
                .setSaveConsumer(value -> {
                    enchantedHitColor = value;
                    ParticleEffects.setEnchantedHitColor(value);
                    save();
                }).build());

        category.addEntry(builder.entryBuilder()
                .startBooleanToggle(Text.literal("Rainbow Enchanted Particles"), useRainbowEnchantedColor)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Enable rainbow effect for enchanted hit particles"))
                .setSaveConsumer(value -> {
                    useRainbowEnchantedColor = value;
                    ParticleEffects.setUseRainbowEnchantedColor(value);
                    save();
                }).build());

        category.addEntry(builder.entryBuilder()
                .startFloatField(Text.literal("Rainbow Speed"), rainbowSpeed)
                .setDefaultValue(1.0f)
                .setMin(0.1f)
                .setMax(5.0f)
                .setTooltip(Text.literal("Speed of rainbow color cycling"))
                .setSaveConsumer(value -> {
                    rainbowSpeed = value;
                    ParticleEffects.setRainbowSpeed(value);
                    save();
                }).build());

        category.addEntry(builder.entryBuilder()
                .startBooleanToggle(Text.literal("Always Show Enchanted Hit"), alwaysShowEnchantedHit)
                .setDefaultValue(false)
                .setTooltip(Text.literal("Always shows enchanted hit particles on weapon hits"))
                .setSaveConsumer(value -> {
                    alwaysShowEnchantedHit = value;
                    ParticleEffects.setAlwaysShowEnchantedHit(value);
                    save();
                }).build());

        return category;
    }

    @Override
    public Text getCategoryText() {
        return Text.literal("Particles");
    }
}