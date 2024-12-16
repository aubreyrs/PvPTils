package cat.aubrey.pvptils.client.mixins;

import cat.aubrey.pvptils.client.Pvptils;
import cat.aubrey.pvptils.client.data.PlayerAttackTracker;
import cat.aubrey.pvptils.client.modules.ParticleEffects;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SwordItem;
import net.minecraft.item.AxeItem;
import net.minecraft.network.packet.s2c.play.EntityAnimationS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerInteractionManager.class)
public class ClientPlayerInteractionManagerMixin {
    @Inject(method = "attackEntity", at = @At("HEAD"))
    private void onAttackEntity(PlayerEntity player, Entity target, CallbackInfo ci) {
        PlayerAttackTracker.setLastAttackedEntityId(target.getId());
        ItemStack mainHandStack = player.getMainHandStack();
        Item usedItem = mainHandStack.getItem();
        PlayerAttackTracker.setLastAttackedWithItem(usedItem);

        if (player instanceof ClientPlayerEntity clientPlayer && clientPlayer.networkHandler != null) {
            if (ParticleEffects.shouldAlwaysShowEnchantedHit() && (usedItem instanceof SwordItem || usedItem instanceof AxeItem)) {
                int particleCount = (int) ParticleEffects.getSharpnessParticleMultiplier();
                for (int i = 0; i < particleCount; i++) {
                    EntityAnimationS2CPacket packet = new EntityAnimationS2CPacket(target, EntityAnimationS2CPacket.ENCHANTED_HIT);
                    clientPlayer.networkHandler.onEntityAnimation(packet);
                }
            }

            if (clientPlayer.fallDistance > 0.0f && !clientPlayer.isOnGround()
                    && !clientPlayer.isClimbing() && !clientPlayer.isTouchingWater()) {
                int critParticleCount = (int) ParticleEffects.getCriticalParticleMultiplier();
                for (int i = 0; i < critParticleCount; i++) {
                    EntityAnimationS2CPacket packet = new EntityAnimationS2CPacket(target, EntityAnimationS2CPacket.CRIT);
                    clientPlayer.networkHandler.onEntityAnimation(packet);
                }
            }
        }
    }
}