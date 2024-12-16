package cat.aubrey.pvptils.client.mixins;

import cat.aubrey.pvptils.client.modules.ParticleEffects;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ParticleManager.class)
public abstract class ParticleManagerMixin {
    @Shadow
    protected abstract Particle createParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ);

    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;",
            at = @At("RETURN"), cancellable = true)
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z,
                               double velocityX, double velocityY, double velocityZ,
                               CallbackInfoReturnable<Particle> cir) {
        Particle particle = cir.getReturnValue();
        if (particle == null) return;

        if (parameters.getType() == ParticleTypes.CRIT) {
            particle.scale(ParticleEffects.getCriticalParticleScale());
            if (ParticleEffects.shouldUseRainbowCriticalColor()) {
                int color = ParticleEffects.getRainbowColor();
                float red = ((color >> 16) & 0xFF) / 255.0F;
                float green = ((color >> 8) & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                particle.setColor(red, green, blue);
            } else if (ParticleEffects.shouldUseCustomCriticalColor()) {
                int color = ParticleEffects.getCriticalParticleColor();
                float red = ((color >> 16) & 0xFF) / 255.0F;
                float green = ((color >> 8) & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                particle.setColor(red, green, blue);
            }

            int particleCount = Math.max(0, (int)(ParticleEffects.getCriticalParticleMultiplier() - 1));
            for (int i = 0; i < particleCount; i++) {
                Particle extraParticle = this.createParticle(parameters, x, y, z,
                        velocityX + (Math.random() - 0.5) * 0.1,
                        velocityY + (Math.random() - 0.5) * 0.1,
                        velocityZ + (Math.random() - 0.5) * 0.1);
                if (extraParticle != null) {
                    extraParticle.scale(ParticleEffects.getCriticalParticleScale());
                    if (ParticleEffects.shouldUseRainbowCriticalColor()) {
                        int color = ParticleEffects.getRainbowColor();
                        float red = ((color >> 16) & 0xFF) / 255.0F;
                        float green = ((color >> 8) & 0xFF) / 255.0F;
                        float blue = (color & 0xFF) / 255.0F;
                        extraParticle.setColor(red, green, blue);
                    } else if (ParticleEffects.shouldUseCustomCriticalColor()) {
                        int color = ParticleEffects.getCriticalParticleColor();
                        float red = ((color >> 16) & 0xFF) / 255.0F;
                        float green = ((color >> 8) & 0xFF) / 255.0F;
                        float blue = (color & 0xFF) / 255.0F;
                        extraParticle.setColor(red, green, blue);
                    }
                }
            }
        } else if (parameters.getType() == ParticleTypes.ENCHANTED_HIT) {
            particle.scale(ParticleEffects.getSharpnessParticleScale());
            if (ParticleEffects.shouldUseRainbowEnchantedColor()) {
                int color = ParticleEffects.getRainbowColor();
                float red = ((color >> 16) & 0xFF) / 255.0F;
                float green = ((color >> 8) & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                particle.setColor(red, green, blue);
            } else if (ParticleEffects.shouldUseCustomEnchantedColor()) {
                int color = ParticleEffects.getEnchantedHitColor();
                float red = ((color >> 16) & 0xFF) / 255.0F;
                float green = ((color >> 8) & 0xFF) / 255.0F;
                float blue = (color & 0xFF) / 255.0F;
                particle.setColor(red, green, blue);
            }

            int particleCount = Math.max(0, (int)(ParticleEffects.getSharpnessParticleMultiplier() - 1));
            for (int i = 0; i < particleCount; i++) {
                Particle extraParticle = this.createParticle(parameters, x, y, z,
                        velocityX + (Math.random() - 0.5) * 0.1,
                        velocityY + (Math.random() - 0.5) * 0.1,
                        velocityZ + (Math.random() - 0.5) * 0.1);
                if (extraParticle != null) {
                    extraParticle.scale(ParticleEffects.getSharpnessParticleScale());
                    if (ParticleEffects.shouldUseRainbowEnchantedColor()) {
                        int color = ParticleEffects.getRainbowColor();
                        float red = ((color >> 16) & 0xFF) / 255.0F;
                        float green = ((color >> 8) & 0xFF) / 255.0F;
                        float blue = (color & 0xFF) / 255.0F;
                        extraParticle.setColor(red, green, blue);
                    } else if (ParticleEffects.shouldUseCustomEnchantedColor()) {
                        int color = ParticleEffects.getEnchantedHitColor();
                        float red = ((color >> 16) & 0xFF) / 255.0F;
                        float green = ((color >> 8) & 0xFF) / 255.0F;
                        float blue = (color & 0xFF) / 255.0F;
                        extraParticle.setColor(red, green, blue);
                    }
                }
            }
        }
    }
}