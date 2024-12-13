package cat.aubrey.pvptils.client.mixins;

import cat.aubrey.pvptils.client.Pvptils;
import cat.aubrey.pvptils.client.data.PlayerAttackTracker;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
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
//        Pvptils.LOGGER.info("Player attacked entity {} with ID {} using item {}", target.getName().getString(), target.getId(), usedItem.getTranslationKey());
    }
}
