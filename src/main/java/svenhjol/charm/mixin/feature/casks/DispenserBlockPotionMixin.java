package svenhjol.charm.mixin.feature.casks;

import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.DefaultDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.casks.Casks;
import svenhjol.charm.feature.casks.common.CaskBlockEntity;

@Mixin(targets = {"net/minecraft/core/dispenser/DispenseItemBehavior$18"})
public abstract class DispenserBlockPotionMixin extends DefaultDispenseItemBehavior {

    @Inject(
        method = "execute",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookPotionCheck(BlockSource blockSource, ItemStack stack, CallbackInfoReturnable<ItemStack> cir) {
        var serverLevel = blockSource.level();
        var dispenserState = blockSource.state();
        var pos = blockSource.pos().relative(dispenserState.getValue(DispenserBlock.FACING));

        if (serverLevel.getBlockEntity(pos) instanceof CaskBlockEntity cask) {
            var result = Resolve.feature(Casks.class).handlers.dispenserAddToCask(cask, stack);
            if (result) {
                stack.shrink(1);
                cir.setReturnValue(stack);
            }
        }

        // Return control back to the default execute method.
    }
}
