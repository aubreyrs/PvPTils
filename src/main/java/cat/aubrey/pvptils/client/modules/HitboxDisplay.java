package cat.aubrey.pvptils.client.modules;

public class HitboxDisplay {
    private static int hitboxColor = 0xFF0000;
    private static boolean showEyeHeight = false;
    private static boolean showLookVector = false;
    private static float hitboxScale = 1.0f;

    public static int getHitboxColor() {
        return hitboxColor;
    }

    public static void setHitboxColor(int color) {
        hitboxColor = color;
    }

    public static float getHitboxScale() {
        return hitboxScale;
    }

    public static void setHitboxScale(float scale) {
        hitboxScale = scale;
    }

    public static float getRedComponent() {
        return ((hitboxColor >> 16) & 0xFF) / 255.0f;
    }

    public static float getGreenComponent() {
        return ((hitboxColor >> 8) & 0xFF) / 255.0f;
    }

    public static float getBlueComponent() {
        return (hitboxColor & 0xFF) / 255.0f;
    }

    public static boolean shouldShowEyeHeight() {
        return showEyeHeight;
    }

    public static boolean shouldShowLookVector() {
        return showLookVector;
    }

    public static void setShowEyeHeight(boolean show) {
        showEyeHeight = show;
    }

    public static void setShowLookVector(boolean show) {
        showLookVector = show;
    }
}
