package svenhjol.charm.mixin.feature.colored_glints;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.colored_glints.ClientCallbacks;
import svenhjol.charm.feature.colored_glints.ColoredGlintsClient;

@Mixin(RenderType.class)
public class RenderTypeMixin {
    @Inject(
        method = "armorGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetArmorGlint(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlintsClient.enabled) {
            cir.setReturnValue(ClientCallbacks.getArmorGlintRenderLayer());
        }
    }

    @Inject(
        method = "armorEntityGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetArmorEntityGlint(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlintsClient.enabled) {
            cir.setReturnValue(ClientCallbacks.getArmorEntityGlintRenderLayer());
        }
    }

    @Inject(
        method = "entityGlint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetEntityGlint(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlintsClient.enabled) {
            cir.setReturnValue(ClientCallbacks.getEntityGlintRenderLayer());
        }
    }

    @Inject(
        method = "entityGlintDirect",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetEntityGlintDirect(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlintsClient.enabled) {
            cir.setReturnValue(ClientCallbacks.getDirectEntityGlintRenderLayer());
        }
    }

    @Inject(
        method = "glint",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetGlint(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlintsClient.enabled) {
            cir.setReturnValue(ClientCallbacks.getGlintRenderLayer());
        }
    }

    @Inject(
        method = "glintDirect",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookGetGlintDirect(CallbackInfoReturnable<RenderType> cir) {
        if (ColoredGlintsClient.enabled) {
            cir.setReturnValue(ClientCallbacks.getDirectGlintRenderLayer());
        }
    }
}
