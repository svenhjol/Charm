package svenhjol.charm.mixin.extra_stackables;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(PotionItem.class)
public class PotionItemMixin {
    /**
     * Change behavior to drop a bottle if the inventory is full.
     */
    @Redirect(
        method = "finishUsingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"
        )
    )
    private boolean hookGetInventory(Inventory inventory, ItemStack bottle) {
        inventory.player.getInventory().placeItemBackInInventory(bottle);
        return false;
    }
}
