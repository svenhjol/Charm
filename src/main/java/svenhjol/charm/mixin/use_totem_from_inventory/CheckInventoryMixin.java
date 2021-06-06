package svenhjol.charm.mixin.use_totem_from_inventory;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.use_totem_from_inventory.UseTotemFromInventory;

@Mixin(LivingEntity.class)
public class CheckInventoryMixin {
    /**
     * Defer to tryFromInventory when checking if the entity is holding a totem.
     * If the check passes (the entity has one in inventory) then return true.
     */
    @Redirect(
        method = "checkTotemDeathProtection",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/LivingEntity;getItemInHand(Lnet/minecraft/world/InteractionHand;)Lnet/minecraft/world/item/ItemStack;"
        )
    )
    private ItemStack hookTryUseTotem(LivingEntity livingEntity, InteractionHand hand) {
        return UseTotemFromInventory.tryFromInventory(livingEntity, hand);
    }
}
