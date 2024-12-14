package cat.aubrey.pvptils.client.modules;

public class ParticleEffects {
    private static float criticalParticleScale = 1.0f;
    private static float sharpnessParticleScale = 1.0f;

    public static float getCriticalParticleScale() {
        return criticalParticleScale;
    }

    public static void setCriticalParticleScale(float scale) {
        criticalParticleScale = scale;
    }

    public static float getSharpnessParticleScale() {
        return sharpnessParticleScale;
    }

    public static void setSharpnessParticleScale(float scale) {
        sharpnessParticleScale = scale;
    }
}