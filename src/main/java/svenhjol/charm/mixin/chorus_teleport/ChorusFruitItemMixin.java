package svenhjol.charm.mixin.chorus_teleport;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ChorusFruitItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.chorus_teleport.ChorusTeleport;

@Mixin(ChorusFruitItem.class)
public class ChorusFruitItemMixin {
    @Inject(
        method = "finishUsingItem",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookFinishUsing(ItemStack stack, Level world, LivingEntity entity, CallbackInfoReturnable<ItemStack> cir) {
        if (ChorusTeleport.tryChorusTeleport(entity, stack)) {
            cir.setReturnValue(stack);
        }
    }
}
