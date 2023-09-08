package svenhjol.charm.mixin.storage_blocks;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.storage_blocks.ender_pearls.EnderPearls;

@Mixin(ChorusFruitItem.class)
public class ChorusFruitItemMixin {
    @Inject(
        method = "finishUsingItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookFinishUsing(ItemStack stack, Level world, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (EnderPearls.tryChorusTeleport(entity, stack)) {
            cir.setReturnValue(stack);
        }
    }
}
