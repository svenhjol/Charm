package svenhjol.charm.mixin.feature.casks;

import net.minecraft.core.dispenser.BlockSource;
import net.minecraft.core.dispenser.OptionalDispenseItemBehavior;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.DispenserBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.casks.Casks;
import svenhjol.charm.feature.casks.common.CaskBlockEntity;

@Mixin(targets = {"net/minecraft/core/dispenser/DispenseItemBehavior$14"})
public abstract class DispenserBlockGlassBottleMixin extends OptionalDispenseItemBehavior {
    @Shadow protected abstract ItemStack takeLiquid(BlockSource blockSource, ItemStack itemStack, ItemStack itemStack2);

    @Inject(
        method = "execute",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGlassBottleCheck(BlockSource blockSource, ItemStack itemStack, CallbackInfoReturnable<ItemStack> cir) {
        var serverLevel = blockSource.level();
        var dispenserState = blockSource.state();
        var pos = blockSource.pos().relative(dispenserState.getValue(DispenserBlock.FACING));

        if (serverLevel.getBlockEntity(pos) instanceof CaskBlockEntity cask) {
            var opt = Resolve.feature(Casks.class).handlers.dispenserTakeFromCask(cask);
            if (opt.isPresent()) {
                // Add back to dispenser and exit from this method early to avoid any side-effects.
                this.setSuccess(true);
                cir.setReturnValue(this.takeLiquid(blockSource, itemStack, opt.get()));
            }
        }
        
        // Return control back to the default execute method.
    }
}
