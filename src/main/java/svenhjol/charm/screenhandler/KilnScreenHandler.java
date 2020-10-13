package svenhjol.charm.screenhandler;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.recipe.book.RecipeBookCategory;
import net.minecraft.screen.AbstractFurnaceScreenHandler;
import net.minecraft.screen.PropertyDelegate;
import svenhjol.charm.module.Kilns;

public class KilnScreenHandler extends AbstractFurnaceScreenHandler {
    public KilnScreenHandler(int syncId, PlayerInventory playerInventory) {
        super(Kilns.SCREEN_HANDLER, Kilns.RECIPE_TYPE, RecipeBookCategory.SMOKER, syncId, playerInventory);
    }

    public KilnScreenHandler(int syncId, PlayerInventory playerInventory, Inventory inventory, PropertyDelegate propertyDelegate) {
        super(Kilns.SCREEN_HANDLER, Kilns.RECIPE_TYPE, RecipeBookCategory.SMOKER, syncId, playerInventory, inventory, propertyDelegate);
    }
}
