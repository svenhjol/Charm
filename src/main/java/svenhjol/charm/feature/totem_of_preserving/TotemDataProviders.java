package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony_api.enums.TotemType;
import svenhjol.charmony_api.iface.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TotemDataProviders implements
    ITotemPreservingProvider,
    ITotemInventoryCheckProvider,
    IRecipeRemoveProvider,
    IAdvancementRemoveProvider
{
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

    @Override
    public Optional<ItemStack> findTotemFromInventory(Player player, TotemType totemType) {
        if (totemType == TotemType.PRESERVING) {
            var totem = TotemOfPreserving.item.get();

            var mainHand = player.getMainHandItem();
            if (mainHand.is(totem)) {
                return Optional.of(mainHand);
            }

            var offHand = player.getOffhandItem();
            if (offHand.is(totem)) {
                return Optional.of(offHand);
            }

            for (var item : player.getInventory().items) {
                if (item.is(totem)) {
                    return Optional.of(item);
                }
            }
        }

        return Optional.empty();
    }

    @Override
    public List<IRecipeFilter> getRecipeFilters() {
        return List.of(new IRecipeFilter() {
            @Override
            public boolean test() {
                return TotemOfPreserving.graveMode;
            }

            @Override
            public List<String> removes() {
                return List.of(
                    "charm:totem_of_preserving/*"
                );
            }
        });
    }

    @Override
    public List<IAdvancementFilter> getAdvancementFilters() {
        return List.of(new IAdvancementFilter() {
            @Override
            public boolean test() {
                return TotemOfPreserving.graveMode;
            }

            @Override
            public List<String> removes() {
                return List.of(
                    "charm:totem_of_preserving/recipes/*"
                );
            }
        });
    }
}
