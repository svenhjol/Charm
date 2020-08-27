package svenhjol.charm.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.BlockOfEnderPearls;

@Mixin(ChorusFruitItem.class)
public class ChorusFruitItemMixin {
    @Inject(
        method = "finishUsing",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookFinishUsing(ItemStack stack, World world, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (BlockOfEnderPearls.tryChorusTeleport(entity, stack))
            cir.setReturnValue(stack);
    }
}
