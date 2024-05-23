package svenhjol.charm.feature.crafting_from_inventory.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.api.iface.CraftingTableInventoryCheckProvider;
import svenhjol.charm.feature.crafting_from_inventory.CraftingFromInventory;
import svenhjol.charm.foundation.feature.ProviderHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Providers extends ProviderHolder<CraftingFromInventory> implements CraftingTableInventoryCheckProvider {
    public final List<CraftingTableInventoryCheckProvider> inventoryCheckProviders = new ArrayList<>();

    public Providers(CraftingFromInventory feature) {
        super(feature);
    }

    @Override
    public Optional<ItemStack> findCraftingTableFromInventory(Player player) {
        List<ItemStack> held = List.of(player.getMainHandItem(), player.getOffhandItem());

        for (ItemStack item : held) {
            if (item.is(Tags.CRAFTING_TABLES)) {
                return Optional.of(item);
            }
        }

        for (ItemStack item : player.getInventory().items) {
            if (item.is(Tags.CRAFTING_TABLES)) {
                return Optional.of(item);
            }
        }

        return Optional.empty();
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(CraftingTableInventoryCheckProvider.class, inventoryCheckProviders::add);
    }
}
