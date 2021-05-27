package svenhjol.charm.mixin.block_of_ender_pearls;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ChorusFruitItem;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.block_of_ender_pearls.BlockOfEnderPearls;

@Mixin(ChorusFruitItem.class)
public class PreventRandomTeleportMixin {

    /**
     * Defer to {@link BlockOfEnderPearls#tryChorusTeleport} when the player has finished eating a chorus fruit.
     * If the check is successful, return early from the vanilla method.
     * This prevents the vanilla random teleport behavior.
     */
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
