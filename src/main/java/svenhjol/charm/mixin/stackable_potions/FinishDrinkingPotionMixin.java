package svenhjol.charm.mixin.stackable_potions;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.PotionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.helper.PlayerHelper;

@Mixin(PotionItem.class)
public class FinishDrinkingPotionMixin {
    /**
     * Change the call to add a glass bottle to the player's inventory
     * to a call that will drop the bottle if the inventory is full.
     */
    @Redirect(
        method = "finishUsingItem",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/player/Inventory;add(Lnet/minecraft/world/item/ItemStack;)Z"
        )
    )
    private boolean hookGetInventory(Inventory inventory, ItemStack bottle) {
        var player = inventory.player;
        if (player != null) {
            return PlayerHelper.addOrDropStack(player, bottle);
        }
        return false;
    }
}
