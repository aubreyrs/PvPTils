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

@Mixin(ParticleManager.class)
public class ParticleManagerMixin {
    @Inject(method = "addParticle(Lnet/minecraft/particle/ParticleEffect;DDDDDD)Lnet/minecraft/client/particle/Particle;", at = @At("RETURN"))
    private void onAddParticle(ParticleEffect parameters, double x, double y, double z, double velocityX, double velocityY, double velocityZ, CallbackInfoReturnable<Particle> cir) {
        Particle particle = cir.getReturnValue();
        if (particle != null) {
            if (parameters.getType() == ParticleTypes.CRIT) {
                particle.scale(ParticleEffects.getCriticalParticleScale());
            } else if (parameters.getType() == ParticleTypes.ENCHANTED_HIT) {
                particle.scale(ParticleEffects.getSharpnessParticleScale());
            }
        }
    }
}