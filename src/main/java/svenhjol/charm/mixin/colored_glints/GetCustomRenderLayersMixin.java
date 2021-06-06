package svenhjol.charm.mixin.colored_glints;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.colored_glints.ColoredGlintHandler;
import svenhjol.charm.module.colored_glints.ColoredGlints;

@Mixin(RenderType.class)
public class GetCustomRenderLayersMixin {
    @Inject(
        method = "armorGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetArmorGlint(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getArmorGlintRenderLayer());
    }

    @Inject(
        method = "armorEntityGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetArmorEntityGlint(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getArmorEntityGlintRenderLayer());
    }

    @Inject(
        method = "entityGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetEntityGlint(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getEntityGlintRenderLayer());
    }

    @Inject(
        method = "entityGlintDirect",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetEntityGlintDirect(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getDirectEntityGlintRenderLayer());
    }

    @Inject(
        method = "glint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetGlint(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getGlintRenderLayer());
    }

    @Inject(
        method = "glintDirect",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetGlintDirect(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlints.enabled)
            cir.setReturnValue(ColoredGlintHandler.getDirectGlintRenderLayer());
    }
}
