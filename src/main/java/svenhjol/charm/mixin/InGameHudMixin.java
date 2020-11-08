package svenhjol.charm.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.RemoveSpyglassScope;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {
    @Shadow protected abstract void renderOverlay(Identifier identifier, float scale, int i);

    @Shadow @Final private static Identifier SPYGLASS_SCOPE;

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;renderOverlay(Lnet/minecraft/util/Identifier;FI)V"
        )
    )
    public void hookRender(InGameHud inGameHud, Identifier identifier, float scale, int i) {
        if (!RemoveSpyglassScope.shouldRemoveHud())
            renderOverlay(SPYGLASS_SCOPE, 0.5F, -16777216);
    }
}
