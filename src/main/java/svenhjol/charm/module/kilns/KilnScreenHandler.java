package svenhjol.charm.module.kilns;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractFurnaceMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.module.kilns.Kilns;

public class KilnScreenHandler extends AbstractFurnaceMenu {
    public KilnScreenHandler(int syncId, Inventory playerInventory) {
        super(Kilns.SCREEN_HANDLER, Kilns.RECIPE_TYPE, RecipeBookType.FURNACE, syncId, playerInventory);
    }

    public KilnScreenHandler(int syncId, Inventory playerInventory, Container inventory, ContainerData propertyDelegate) {
        super(Kilns.SCREEN_HANDLER, Kilns.RECIPE_TYPE, RecipeBookType.FURNACE, syncId, playerInventory, inventory, propertyDelegate);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        if (player.level != null && !player.level.isClientSide && index == 2) {
            Kilns.triggerFiredItem((ServerPlayer) player);
        }
        return super.quickMoveStack(player, index);
    }
}
