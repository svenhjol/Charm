package svenhjol.charm.mixin.event;

import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.api.event.ItemTooltipImageCallback;

import java.util.Optional;

@Mixin(Item.class)
public class ItemTooltipImageMixin {
    @Inject(
        method = "getTooltipImage",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetTooltipImage(ItemStack itemStack, CallbackInfoReturnable<Optional<TooltipComponent>> cir) {
        Optional<TooltipComponent> opt = ItemTooltipImageCallback.EVENT.invoker().interact(itemStack);
        if (opt.isPresent()) {
            cir.setReturnValue(opt);
        }
    }
}
