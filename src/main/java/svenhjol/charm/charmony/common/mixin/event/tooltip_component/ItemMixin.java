package svenhjol.charm.charmony.common.mixin.event.tooltip_component;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.charmony.event.TooltipComponentEvent;

import java.util.Optional;

@Mixin(Item.class)
public class ItemMixin {
    @Inject(
        method = "getTooltipImage",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetTooltipImage(ItemStack stack, CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        var tooltip = TooltipComponentEvent.INSTANCE.invoke(stack);

        if (tooltip.isPresent()) {
            cir.setReturnValue(tooltip);
        }
    }
}
