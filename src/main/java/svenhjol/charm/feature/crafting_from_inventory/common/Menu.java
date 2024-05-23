package svenhjol.charm.feature.crafting_from_inventory.common;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;

public class Menu extends CraftingMenu {
    public Menu(int syncId, Inventory inventory, ContainerLevelAccess access) {
        super(syncId, inventory, access);
    }

    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}
