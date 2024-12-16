package cat.aubrey.pvptils.client;

import cat.aubrey.pvptils.client.config.base.ConfigRegistry;
import cat.aubrey.pvptils.client.config.modules.*;
import net.minecraft.client.gui.screen.Screen;

public class Config {
    private static Config INSTANCE;
    private final ConfigRegistry registry;
    private final HitboxConfig hitboxConfig;
    private final ParticleConfig particleConfig;
    private final SpotifyConfig spotifyConfig;
    private final KillEffectConfig killEffectConfig;

    private Config() {
        this.registry = new ConfigRegistry();
        this.hitboxConfig = new HitboxConfig();
        this.particleConfig = new ParticleConfig();
        this.spotifyConfig = new SpotifyConfig();
        this.killEffectConfig = new KillEffectConfig();
        registry.register("hitbox", hitboxConfig);
        registry.register("particles", particleConfig);
        registry.register("spotify", spotifyConfig);
        registry.register("killeffect", killEffectConfig);
        registry.loadAll();
    }

    public static Config get() {
        if (INSTANCE == null) {
            INSTANCE = new Config();
        }
        return INSTANCE;
    }

    public Screen buildScreen(Screen parent) {
        return registry.buildScreen(parent);
    }

    public void save() {
        registry.saveAll();
    }

    public int getHitboxColor() { return hitboxConfig.getHitboxColor(); }
    public float getHitboxScale() { return hitboxConfig.getHitboxScale(); }
    public boolean shouldShowEyeHeight() { return hitboxConfig.shouldShowEyeHeight(); }
    public boolean shouldShowLookVector() { return hitboxConfig.shouldShowLookVector(); }
    public boolean isSpotifyEnabled() { return spotifyConfig.isEnabled(); }
    public boolean isSpotifyPriorityEnabled() { return spotifyConfig.isPriorityEnabled(); }
    public boolean isSpotifyKeybindsEnabled() { return spotifyConfig.isKeybindsEnabled(); }
    public boolean isSpotifyHudEnabled() { return spotifyConfig.isHudEnabled(); }
    public int getSpotifyHudColor() { return spotifyConfig.getHudColor(); }
    public int getSpotifyHudPosition() { return spotifyConfig.getHudPosition(); }
    public boolean isLightningKillEffectEnabled() { return killEffectConfig.isLightningKillEffectEnabled(); }
}