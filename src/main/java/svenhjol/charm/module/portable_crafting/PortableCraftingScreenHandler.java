package svenhjol.charm.module.portable_crafting;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;

public class PortableCraftingScreenHandler extends CraftingMenu {
    public PortableCraftingScreenHandler(int syncId, Inventory playerInventory, ContainerLevelAccess context) {
        super(syncId, playerInventory, context);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
