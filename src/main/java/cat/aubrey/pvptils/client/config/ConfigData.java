package cat.aubrey.pvptils.client.config;

public class ConfigData {
    private boolean lightningKillEffect = true;
    private int hitboxColor = 0xFF0000;
    private boolean showEyeHeight = false;
    private boolean showLookVector = false;
    private float hitboxScale = 1.0f;
    private boolean spotifyEnabled = false;
    private boolean spotifyPriority = true;
    private boolean spotifyKeybindsEnabled = true;
    private boolean spotifyHudEnabled = true;
    private int spotifyHudColor = 0xFFFFFF;
    private int spotifyHudPosition = 0;
    private float criticalParticleScale = 1.0f;
    private float sharpnessParticleScale = 1.0f;
    private float particleMultiplier = 1.0f;
    private boolean alwaysShowEnchantedHit = false;
    public boolean isLightningKillEffectEnabled() { return lightningKillEffect; }
    public int getHitboxColor() { return hitboxColor; }
    public boolean shouldShowEyeHeight() { return showEyeHeight; }
    public boolean shouldShowLookVector() { return showLookVector; }
    public float getHitboxScale() { return hitboxScale; }
    public boolean isSpotifyEnabled() { return spotifyEnabled; }
    public boolean isSpotifyPriorityEnabled() { return spotifyPriority; }
    public boolean isSpotifyKeybindsEnabled() { return spotifyKeybindsEnabled; }
    public boolean isSpotifyHudEnabled() { return spotifyHudEnabled; }
    public int getSpotifyHudColor() { return spotifyHudColor; }
    public int getSpotifyHudPosition() { return spotifyHudPosition; }
    public float getCriticalParticleScale() { return criticalParticleScale; }
    public float getSharpnessParticleScale() { return sharpnessParticleScale; }
    public void setLightningKillEffect(boolean value) { lightningKillEffect = value; }
    public void setHitboxColor(int value) { hitboxColor = value; }
    public void setShowEyeHeight(boolean value) { showEyeHeight = value; }
    public void setShowLookVector(boolean value) { showLookVector = value; }
    public void setHitboxScale(float value) { hitboxScale = value; }
    public void setSpotifyEnabled(boolean value) { spotifyEnabled = value; }
    public void setSpotifyPriority(boolean value) { spotifyPriority = value; }
    public void setSpotifyKeybindsEnabled(boolean value) { spotifyKeybindsEnabled = value; }
    public void setSpotifyHudEnabled(boolean value) { spotifyHudEnabled = value; }
    public void setSpotifyHudColor(int value) { spotifyHudColor = value; }
    public void setSpotifyHudPosition(int value) { spotifyHudPosition = value; }
    public void setCriticalParticleScale(float value) { criticalParticleScale = value; }
    public void setSharpnessParticleScale(float value) { sharpnessParticleScale = value; }
}