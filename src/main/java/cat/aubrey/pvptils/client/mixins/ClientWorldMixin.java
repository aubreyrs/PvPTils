package cat.aubrey.pvptils.client.mixins;

import cat.aubrey.pvptils.client.Pvptils;
import cat.aubrey.pvptils.client.data.PlayerAttackTracker;
import cat.aubrey.pvptils.client.modules.KillEffect;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.Entity.RemovalReason;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AxeItem;
import net.minecraft.item.Item;
import net.minecraft.item.SwordItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.jetbrains.annotations.Nullable;

@Mixin(ClientWorld.class)
public class ClientWorldMixin {
    @Inject(method = "removeEntity(ILnet/minecraft/entity/Entity$RemovalReason;)V", at = @At("HEAD"))
    private void onRemoveEntity(int entityId, RemovalReason reason, CallbackInfo ci) {
        ClientWorld world = (ClientWorld)(Object)this;
        @Nullable Entity entity = world.getEntityById(entityId);
        if (entity != null) {
//            Pvptils.LOGGER.info("Removing entity: {} with reason: {}", entity.getName().getString(), reason);
            int lastAttackedId = PlayerAttackTracker.getLastAttackedEntityId();
            Item lastUsedItem = PlayerAttackTracker.getLastAttackedWithItem();
            if (entity instanceof PlayerEntity && entityId == lastAttackedId && isWeapon(lastUsedItem)) {
//                Pvptils.LOGGER.info("Entity {} likely killed by player with a weapon, triggering kill effect.", entity.getName().getString());
                KillEffect.triggerKillEffect(entity);
            }
        }
    }

    private boolean isWeapon(Item item) {
        return item instanceof SwordItem || item instanceof AxeItem;
    }
}
