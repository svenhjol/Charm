package svenhjol.charm.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.UseTotemFromInventory;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Redirect(
        method = "tryUseTotem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/entity/LivingEntity;getStackInHand(Lnet/minecraft/util/Hand;)Lnet/minecraft/item/ItemStack;"
        )
    )
    private ItemStack hookTryUseTotem(LivingEntity livingEntity, Hand hand) {
        return UseTotemFromInventory.tryFromInventory(livingEntity, hand);
    }
}
