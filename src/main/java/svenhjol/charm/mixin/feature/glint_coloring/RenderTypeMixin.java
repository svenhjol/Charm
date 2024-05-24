package svenhjol.charm.mixin.feature.glint_coloring;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.glint_coloring.GlintColoringClient;
import svenhjol.charm.feature.glint_coloring.client.Handlers;
import svenhjol.charm.foundation.Resolve;

@Mixin(RenderType.class)
public class RenderTypeMixin {
    @Inject(
        method = "armorEntityGlint",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void hookGetArmorEntityGlint(CallbackInfoReturnable<RenderType> cir) {
        var layer = glintColoring().getArmorEntityGlintRenderLayer();
        cir.setReturnValue(layer != null ? layer : cir.getReturnValue());
    }

    @Inject(
        method = "entityGlint",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void hookGetEntityGlint(CallbackInfoReturnable<RenderType> cir) {
        var layer = glintColoring().getEntityGlintRenderLayer();
        cir.setReturnValue(layer != null ? layer : cir.getReturnValue());
    }

    @Inject(
        method = "entityGlintDirect",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void hookGetEntityGlintDirect(CallbackInfoReturnable<RenderType> cir) {
        var layer = glintColoring().getDirectEntityGlintRenderLayer();
        cir.setReturnValue(layer != null ? layer : cir.getReturnValue());
    }

    @Inject(
        method = "glint",
        at = @At("RETURN"),
        cancellable = true
    )
    private static void hookGetGlint(CallbackInfoReturnable<RenderType> cir) {
        var layer = glintColoring().getGlintRenderLayer();
        cir.setReturnValue(layer != null ? layer : cir.getReturnValue());
    }

    @Unique
    private static Handlers glintColoring() {
        return Resolve.feature(GlintColoringClient.class).handlers;
    }
}
