package svenhjol.charm.charmony.client.mixin.registry.client_tooltip_component;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientTooltipComponent.class)
public interface ClientTooltipComponentMixin {
    /**
     * Vanilla hardcodes create() so that only ClientBundleTooltips can be returned.
     * Any other instance that implements ClientTooltipComponent will throw an Exception.
     * This mixin allows any valid ClientTooltipComponent to be returned.
     */
    @Inject(
        method = "create(Lnet/minecraft/world/inventory/tooltip/TooltipComponent;)Lnet/minecraft/client/gui/screens/inventory/tooltip/ClientTooltipComponent;",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookCreate(TooltipComponent tooltipComponent, CallbackInfoReturnable<ClientTooltipComponent> cir) {
        if (tooltipComponent instanceof ClientTooltipComponent) {
            cir.setReturnValue((ClientTooltipComponent)tooltipComponent);
        }
    }
}
