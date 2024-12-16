package cat.aubrey.pvptils.client.modules;

public class ParticleEffects {
    private static float criticalParticleScale = 1.0f;
    private static float sharpnessParticleScale = 1.0f;
    private static boolean alwaysShowEnchantedHit = false;
    private static float criticalParticleMultiplier = 1.0f;
    private static float sharpnessParticleMultiplier = 1.0f;
    private static int criticalParticleColor = 0xFFFFFF;
    private static int enchantedHitColor = 0x8B00FF;
    private static boolean useCustomCriticalColor = false;
    private static boolean useCustomEnchantedColor = false;
    private static boolean useRainbowCriticalColor = false;
    private static boolean useRainbowEnchantedColor = false;
    private static float rainbowSpeed = 1.0f;
    private static float rainbowOffset = 0.0f;

    public static float getCriticalParticleScale() {
        return criticalParticleScale;
    }

    public static void setCriticalParticleScale(float scale) {
        criticalParticleScale = scale;
    }

    public static float getCriticalParticleMultiplier() {
        return criticalParticleMultiplier;
    }

    public static void setCriticalParticleMultiplier(float multiplier) {
        criticalParticleMultiplier = Math.max(1.0f, multiplier);
    }

    public static int getCriticalParticleColor() {
        return criticalParticleColor;
    }

    public static void setCriticalParticleColor(int color) {
        criticalParticleColor = color;
    }

    public static boolean shouldUseCustomCriticalColor() {
        return useCustomCriticalColor;
    }

    public static void setUseCustomCriticalColor(boolean value) {
        useCustomCriticalColor = value;
    }

    public static boolean shouldUseRainbowCriticalColor() {
        return useRainbowCriticalColor;
    }

    public static void setUseRainbowCriticalColor(boolean value) {
        useRainbowCriticalColor = value;
    }

    public static float getSharpnessParticleScale() {
        return sharpnessParticleScale;
    }

    public static void setSharpnessParticleScale(float scale) {
        sharpnessParticleScale = scale;
    }

    public static float getSharpnessParticleMultiplier() {
        return sharpnessParticleMultiplier;
    }

    public static void setSharpnessParticleMultiplier(float multiplier) {
        sharpnessParticleMultiplier = Math.max(1.0f, multiplier);
    }

    public static int getEnchantedHitColor() {
        return enchantedHitColor;
    }

    public static void setEnchantedHitColor(int color) {
        enchantedHitColor = color;
    }

    public static boolean shouldUseCustomEnchantedColor() {
        return useCustomEnchantedColor;
    }

    public static void setUseCustomEnchantedColor(boolean value) {
        useCustomEnchantedColor = value;
    }

    public static boolean shouldUseRainbowEnchantedColor() {
        return useRainbowEnchantedColor;
    }

    public static void setUseRainbowEnchantedColor(boolean value) {
        useRainbowEnchantedColor = value;
    }

    public static boolean shouldAlwaysShowEnchantedHit() {
        return alwaysShowEnchantedHit;
    }

    public static void setAlwaysShowEnchantedHit(boolean value) {
        alwaysShowEnchantedHit = value;
    }

    public static float getRainbowSpeed() {
        return rainbowSpeed;
    }

    public static void setRainbowSpeed(float speed) {
        rainbowSpeed = speed;
    }

    public static int getRainbowColor() {
        float hue = (System.currentTimeMillis() % 3000) / 3000.0f * rainbowSpeed + rainbowOffset;
        return java.awt.Color.HSBtoRGB(hue, 1.0f, 1.0f);
    }

    public static void setRainbowOffset(float offset) {
        rainbowOffset = offset;
    }

    public static float getRainbowOffset() {
        return rainbowOffset;
    }
}