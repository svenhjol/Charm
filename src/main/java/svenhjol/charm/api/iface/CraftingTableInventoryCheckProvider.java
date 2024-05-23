package svenhjol.charm.api.iface;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;

public interface CraftingTableInventoryCheckProvider {
    /**
     * Find a crafting table in any player inventory and return the found crafting table item stack.
     * @param player Player to search inventory.
     * @return Item stack of the crafting table.
     */
    Optional<ItemStack> findCraftingTableFromInventory(Player player);
}
