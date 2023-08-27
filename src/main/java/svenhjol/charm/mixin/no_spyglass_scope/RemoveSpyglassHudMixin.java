package svenhjol.charm.mixin.no_spyglass_scope;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.no_spyglass_scope.NoSpyglassScope;

@Mixin(Gui.class)
public abstract class RemoveSpyglassHudMixin {
    @Shadow private float scopeScale;

    @Shadow protected abstract void renderSpyglassOverlay(GuiGraphics guiGraphics, float scopeScale);

    /**
     * Defer to shouldRemoveHud. If the check is false, render as normal.
     */
    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/Gui;renderSpyglassOverlay(Lnet/minecraft/client/gui/GuiGraphics;F)V"
        )
    )
    public void hookRender(Gui instance, GuiGraphics guiGraphics, float f) {
        if (!NoSpyglassScope.shouldRemoveHud()) {
            this.renderSpyglassOverlay(guiGraphics, this.scopeScale);
        }
    }
}
