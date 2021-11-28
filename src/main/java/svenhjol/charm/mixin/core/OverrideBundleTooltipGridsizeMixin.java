package svenhjol.charm.mixin.core;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.client.CharmItemTooltip;

@Mixin(ClientBundleTooltip.class)
public class OverrideBundleTooltipGridsizeMixin {
    private static BundleTooltip storedBundleTooltip;

    @Inject(
        method = "<init>",
        at = @At("TAIL")
    )
    private void hookInit(BundleTooltip bundleTooltip, CallbackInfo ci) {
        storedBundleTooltip = bundleTooltip;
    }

    @Inject(
        method = "gridSizeX",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookGridSizeX(CallbackInfoReturnable<Integer> cir) {
        if (storedBundleTooltip instanceof CharmItemTooltip) {
            cir.setReturnValue(((CharmItemTooltip) storedBundleTooltip).gridSizeX());
        }
    }

    @Inject(
        method = "gridSizeY",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookGridSizeY(CallbackInfoReturnable<Integer> cir) {
        if (storedBundleTooltip instanceof CharmItemTooltip) {
            cir.setReturnValue(((CharmItemTooltip) storedBundleTooltip).gridSizeY());
        }
    }
}
