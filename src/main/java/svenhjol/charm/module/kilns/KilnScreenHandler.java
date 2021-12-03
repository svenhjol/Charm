package svenhjol.charm.module.kilns;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;

public class KilnScreenHandler extends AbstractFurnaceMenu {
    public KilnScreenHandler(int syncId, Inventory playerInventory) {
        super(Kilns.SCREEN_HANDLER, Kilns.RECIPE_TYPE, RecipeBookType.valueOf("KILN"), syncId, playerInventory);
    }

    public KilnScreenHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(Kilns.SCREEN_HANDLER, Kilns.RECIPE_TYPE, RecipeBookType.valueOf("KILN"), syncId, playerInventory, inventory, propertyDelegate);
    }

    @Override
    protected void doClick(int i, int j, ClickType clickType, Player player) {
        if (player.level != null && !player.level.isClientSide && i == 2 && clickType == ClickType.PICKUP) {
            Kilns.triggerFiredItem((ServerPlayer) player);
        }
        super.doClick(i, j, clickType, player);
    }
}
