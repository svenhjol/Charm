package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony_api.iface.ITotemPreservingProvider;

import java.util.ArrayList;
import java.util.List;

public class TotemInventoryProvider implements ITotemPreservingProvider {
    @Override
    public List<ItemStack> getInventoryItemsForTotem(Player player) {
        List<ItemStack> out = new ArrayList<>();
        var inventory = player.getInventory();

        out.addAll(inventory.items);
        out.addAll(inventory.armor);
        out.addAll(inventory.offhand);

        return out;
    }

    @Override
    public void deleteInventoryItems(Player player) {
        var inventory = player.getInventory();

        inventory.items.clear();
        inventory.armor.clear();
        inventory.offhand.clear();
    }
}
