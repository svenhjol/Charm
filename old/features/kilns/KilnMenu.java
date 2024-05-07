package svenhjol.charm.feature.kilns;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import svenhjol.charm.feature.firing.Firing;

public class KilnMenu extends AbstractFurnaceMenu {
    public KilnMenu(int syncId, Inventory playerInventory) {
        super(Kilns.menu.get(), Firing.recipeType.get(), RecipeBookType.valueOf("KILN"), syncId, playerInventory);
    }

    public KilnMenu(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(Kilns.menu.get(), Firing.recipeType.get(), RecipeBookType.valueOf("KILN"), syncId, playerInventory, inventory, propertyDelegate);
    }

    @Override
    public void clicked(int slot, int button, ClickType clickType, Player player) {
        if (!player.level().isClientSide
            && slot == 2
            && clickType == ClickType.PICKUP) {
            Kilns.triggerFiredItem(player);
        }
        super.clicked(slot, button, clickType, player);
    }
}
