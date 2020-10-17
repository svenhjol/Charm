package svenhjol.charm.mixin;

import net.minecraft.block.entity.ShulkerBoxBlockEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.Crates;

@Mixin(ShulkerBoxBlockEntity.class)
public class ShulkerBoxBlockEntityMixin {
    @Inject(
        method = "canInsert",
        at = @At("HEAD"),
        cancellable = true
    )
    private void canInsertItemHook(int index, ItemStack stack, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (!Crates.canShulkerBoxInsertItem(stack))
            cir.setReturnValue(false);
    }
}
