package svenhjol.charm.api.iface;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@SuppressWarnings("unused")
public interface ITotemPreservingProvider {
    /**
     * Return all items from inventories that should go into a Totem of Preserving on death.
     * @param player Reference to the dead player.
     * @return All items to be added to totem.
     */
    List<ItemStack> getInventoryItemsForTotem(Player player);

    /**
     * Callback to delete all items from inventories after they've been preserved in a totem.
     * @param player Reference to the dead player.
     */
    void deleteInventoryItems(Player player);
}
