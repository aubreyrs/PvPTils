package cat.aubrey.pvptils.client;

import cat.aubrey.pvptils.client.modules.HitboxDisplay;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class Config {
    private static Config INSTANCE;

    private static boolean lightningKillEffect = true;
    private static int hitboxColor = 0xFF0000;
    private static boolean showEyeHeight = false;
    private static boolean showLookVector = false;
    private static float hitboxScale = 1.0f;

    private Config() {}

    public static Config get() {
        if (INSTANCE == null) INSTANCE = new Config();
        return INSTANCE;
    }

    public boolean isLightningKillEffectEnabled() {
        return lightningKillEffect;
    }

    public int getHitboxColor() {
        return hitboxColor;
    }

    public float getHitboxScale() {
        return hitboxScale;
    }

    public boolean shouldShowEyeHeight() {
        return showEyeHeight;
    }

    public boolean shouldShowLookVector() {
        return showLookVector;
    }

    public Screen buildScreen(Screen parent) {
        ConfigBuilder builder = ConfigBuilder.create().setParentScreen(parent).setTitle(Text.literal("PvPtils Configuration"));
        builder.getOrCreateCategory(Text.literal("General")).addEntry(
                builder.entryBuilder().startBooleanToggle(Text.literal("Lightning Kill Effect"), lightningKillEffect)
                        .setDefaultValue(true)
                        .setTooltip(Text.literal("Shows a lightning effect when an entity dies (client-side)"))
                        .setSaveConsumer(newValue -> lightningKillEffect = newValue).build()
        );
        builder.getOrCreateCategory(Text.literal("Hitboxes"))
                .addEntry(
                        builder.entryBuilder().startColorField(Text.literal("Hitbox Color"), hitboxColor)
                                .setDefaultValue(0xFF0000)
                                .setTooltip(Text.literal("Color of entity hitboxes"))
                                .setSaveConsumer(newValue -> {
                                    hitboxColor = newValue;
                                    HitboxDisplay.setHitboxColor(newValue);
                                }).build()
                )
                .addEntry(
                        builder.entryBuilder().startFloatField(Text.literal("Hitbox Scale"), hitboxScale)
                                .setDefaultValue(1.0f)
                                .setMin(0.1f).setMax(2.0f)
                                .setTooltip(Text.literal("Size multiplier for hitbox display"))
                                .setSaveConsumer(newValue -> {
                                    hitboxScale = newValue;
                                    HitboxDisplay.setHitboxScale(newValue);
                                }).build()
                )
                .addEntry(
                        builder.entryBuilder().startBooleanToggle(Text.literal("Show Eye Height"), showEyeHeight)
                                .setDefaultValue(false)
                                .setTooltip(Text.literal("Show entity eye height indicator"))
                                .setSaveConsumer(newValue -> {
                                    showEyeHeight = newValue;
                                    HitboxDisplay.setShowEyeHeight(newValue);
                                }).build()
                )
                .addEntry(
                        builder.entryBuilder().startBooleanToggle(Text.literal("Show Look Vector"), showLookVector)
                                .setDefaultValue(false)
                                .setTooltip(Text.literal("Show entity look direction indicator"))
                                .setSaveConsumer(newValue -> {
                                    showLookVector = newValue;
                                    HitboxDisplay.setShowLookVector(newValue);
                                }).build()
                );
        return builder.build();
    }
}
