package svenhjol.charm.mixin.no_spyglass_scope;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.no_spyglass_scope.NoSpyglassScope;

@Mixin(Gui.class)
public abstract class RemoveSpyglassHudMixin {
    @Shadow protected abstract void renderSpyglassOverlay(float scale);

    @Shadow private float scopeScale;

    /**
     * Defer to shouldRemoveHud. If the check is false, render as normal.
     */
    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;renderSpyglassOverlay(F)V"
        )
    )
    public void hookRender(Gui inGameHud, float f) {
        if (!NoSpyglassScope.shouldRemoveHud()) {
            this.renderSpyglassOverlay(this.scopeScale);
        }
    }
}
