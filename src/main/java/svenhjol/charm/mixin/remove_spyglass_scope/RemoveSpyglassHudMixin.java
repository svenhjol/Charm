package svenhjol.charm.mixin.remove_spyglass_scope;

import net.minecraft.client.gui.Gui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.remove_spyglass_scope.RemoveSpyglassScope;

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
        if (!RemoveSpyglassScope.shouldRemoveHud())
            this.renderSpyglassOverlay(this.scopeScale);
    }
}
