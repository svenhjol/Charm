package svenhjol.charm.mixin;

import net.minecraft.item.ItemStack;
import net.minecraft.screen.slot.ShulkerBoxSlot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.Crates;

@Mixin(ShulkerBoxSlot.class)
public class ShulkerBoxSlotMixin {
    @Inject(
        method = "canInsert",
        at = @At("HEAD"),
        cancellable = true
    )
    private void isItemValidHook(ItemStack stack, CallbackInfoReturnable<Boolean> cir) {
        if (!Crates.canShulkerBoxInsertItem(stack))
            cir.setReturnValue(false);
    }
}
