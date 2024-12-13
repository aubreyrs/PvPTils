package cat.aubrey.pvptils.client.mixins;

import cat.aubrey.pvptils.client.modules.HitboxDisplay;
import net.minecraft.client.render.WorldRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(WorldRenderer.class)
public class WorldRendererMixin {
    @Inject(method = "drawEntityOutlinesFramebuffer", at = @At("HEAD"), cancellable = true)
    private void onDrawEntityOutlines(CallbackInfo ci) {
        if (!HitboxDisplay.shouldShowEyeHeight() && !HitboxDisplay.shouldShowLookVector()) {
            ci.cancel();
        }
    }
}
