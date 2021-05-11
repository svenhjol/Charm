package svenhjol.charm.screenhandler;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import net.minecraft.server.network.ServerPlayerEntity;
import svenhjol.charm.module.Kilns;

public class KilnScreenHandler extends AbstractFurnaceScreenHandler {
    public KilnScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(Kilns.SCREEN_HANDLER, Kilns.RECIPE_TYPE, RecipeBookCategory.FURNACE, syncId, playerInventory);
    }

    public KilnScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Kilns.SCREEN_HANDLER, Kilns.RECIPE_TYPE, RecipeBookCategory.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }

    @Override
    public ItemStack transferSlot(PlayerEntity player, int index) {
        if (player.world != null && !player.world.isClient && index == 2) {
            Kilns.triggerFiredItem((ServerPlayerEntity) player);
        }
        return super.transferSlot(player, index);
    }
}
