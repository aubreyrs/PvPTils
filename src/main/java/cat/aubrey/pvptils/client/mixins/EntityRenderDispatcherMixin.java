package cat.aubrey.pvptils.client.mixins;

import cat.aubrey.pvptils.client.modules.HitboxDisplay;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexRendering;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
    @ModifyArg(method = "renderHitbox", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexRendering;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/math/Box;FFFF)V"), index = 2)
    private static Box modifyHitboxSize(Box box) {
        float scale = HitboxDisplay.getHitboxScale();
        if (scale != 1.0f) {
            double cx = (box.minX + box.maxX) / 2.0;
            double cy = (box.minY + box.maxY) / 2.0;
            double cz = (box.minZ + box.maxZ) / 2.0;
            double sx = (box.maxX - box.minX) * scale;
            double sy = (box.maxY - box.minY) * scale;
            double sz = (box.maxZ - box.minZ) * scale;
            return new Box(cx - sx / 2.0, cy - sy / 2.0, cz - sz / 2.0, cx + sx / 2.0, cy + sy / 2.0, cz + sz / 2.0);
        }
        return box;
    }

    @ModifyArg(method = "renderHitbox", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexRendering;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/math/Box;FFFF)V"), index = 3)
    private static float modifyRed(float original) {
        return HitboxDisplay.getRedComponent();
    }

    @ModifyArg(method = "renderHitbox", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexRendering;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/math/Box;FFFF)V"), index = 4)
    private static float modifyGreen(float original) {
        return HitboxDisplay.getGreenComponent();
    }

    @ModifyArg(method = "renderHitbox", at = @At(value = "INVOKE",
            target = "Lnet/minecraft/client/render/VertexRendering;drawBox(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumer;Lnet/minecraft/util/math/Box;FFFF)V"), index = 5)
    private static float modifyBlue(float original) {
        return HitboxDisplay.getBlueComponent();
    }

    @Inject(method = "renderHitbox", at = @At("HEAD"), cancellable = true)
    private static void onRenderHitbox(MatrixStack matrices, VertexConsumer vertices, Entity entity, float tickDelta, float red, float green, float blue, CallbackInfo ci) {
        if (!HitboxDisplay.shouldShowEyeHeight() && !HitboxDisplay.shouldShowLookVector()) {
            Box box = entity.getBoundingBox().offset(-entity.getX(), -entity.getY(), -entity.getZ());
            VertexRendering.drawBox(matrices, vertices, box, HitboxDisplay.getRedComponent(), HitboxDisplay.getGreenComponent(), HitboxDisplay.getBlueComponent(), 1.0F);
            ci.cancel();
        }
    }
}
