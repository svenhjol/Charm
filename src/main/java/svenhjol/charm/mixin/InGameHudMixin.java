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
    @Shadow @Final private static Identifier SPYGLASS_SCOPE;

    @Shadow protected abstract void method_32598(float f);

    @Shadow private float field_27959;

    @Redirect(
        method = "render",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/client/gui/hud/InGameHud;method_32598(F)V"
        )
    )
    public void hookRender(InGameHud inGameHud, float f) {
        if (!RemoveSpyglassScope.shouldRemoveHud())
            this.method_32598(this.field_27959);
    }
}
