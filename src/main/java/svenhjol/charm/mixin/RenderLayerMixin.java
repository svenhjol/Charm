package svenhjol.charm.mixin;

import net.minecraft.client.render.RenderLayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.handler.ColoredGlintHandler;
import svenhjol.charm.module.Core;

@Mixin(RenderLayer.class)
public class RenderLayerMixin {
    @Inject(
        method = "getArmorGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetArmorGlint(CallbackInfoReturnable<RenderLayer> cir) {
        if (Core.debug)
            cir.setReturnValue(ColoredGlintHandler.getArmorGlintRenderLayer());
    }

    @Inject(
        method = "getArmorEntityGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetArmorEntityGlint(CallbackInfoReturnable<RenderLayer> cir) {
        if (Core.debug)
            cir.setReturnValue(ColoredGlintHandler.getArmorEntityGlintRenderLayer());
    }

    @Inject(
        method = "getEntityGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetEntityGlint(CallbackInfoReturnable<RenderLayer> cir) {
        if (Core.debug)
            cir.setReturnValue(ColoredGlintHandler.getEntityGlintRenderLayer());
    }

    @Inject(
        method = "getDirectEntityGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetEntityGlintDirect(CallbackInfoReturnable<RenderLayer> cir) {
        if (Core.debug)
            cir.setReturnValue(ColoredGlintHandler.getDirectEntityGlintRenderLayer());
    }

    @Inject(
        method = "getGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetGlint(CallbackInfoReturnable<RenderLayer> cir) {
        if (Core.debug)
            cir.setReturnValue(ColoredGlintHandler.getGlintRenderLayer());
    }

    @Inject(
        method = "getDirectGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetGlintDirect(CallbackInfoReturnable<RenderLayer> cir) {
        if (Core.debug)
            cir.setReturnValue(ColoredGlintHandler.getDirectGlintRenderLayer());
    }
}
