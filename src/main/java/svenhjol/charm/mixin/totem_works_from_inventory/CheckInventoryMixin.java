package svenhjol.charm.mixin.totem_works_from_inventory;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.module.totem_works_from_inventory.TotemWorksFromInventory;

@Mixin(LivingEntity.class)
public class CheckInventoryMixin {
    /**
     * Defer to {@link TotemWorksFromInventory#tryFromInventory} when checking if the entity is holding a totem.
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
        return TotemWorksFromInventory.tryFromInventory(livingEntity, hand);
    }
}
