package svenhjol.charm.mixin.weathering_iron;

import net.minecraft.core.BlockSource;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.weathering_iron.WeatheringIron;

/**
 * Targets the Honeycomb dispense behavior class.
 */
@Mixin(targets = {"net/minecraft/core/dispenser/DispenseItemBehavior$26"})
public class DispenseHoneycombIronMixin {
    @Inject(
        method = "execute",
        at = @At("HEAD"),
        cancellable = true
    )
    public void hookExecute(BlockSource source, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        ItemStack result = WeatheringIron.tryDispense(source, stack);
        if (result != null) {
            cir.setReturnValue(stack);
        }
    }
}
