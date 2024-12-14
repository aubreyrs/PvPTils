package cat.aubrey.pvptils.client.mixins;

import cat.aubrey.pvptils.client.modules.SpotifyHud;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.RenderTickCounter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Inject(method = "render", at = @At("RETURN"))
    private void onRender(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        SpotifyHud.updateCurrentSong();
        SpotifyHud.render(context);
    }
}