package svenhjol.charm.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.RemoveSpyglassScope;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract void renderSpyglassOverlay(float scale);

    @Shadow private float spyglassScale;

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;renderSpyglassOverlay(F)V"
        )
    )
    public void hookRender(InGameHud inGameHud, float f) {
        if (!RemoveSpyglassScope.shouldRemoveHud())
            this.renderSpyglassOverlay(this.spyglassScale);
    }
}
