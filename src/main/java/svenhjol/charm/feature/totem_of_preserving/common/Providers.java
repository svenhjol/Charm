package svenhjol.charm.feature.totem_of_preserving.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.api.enums.TotemType;
import svenhjol.charm.api.iface.*;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.foundation.feature.ProviderHolder;
import svenhjol.charm.foundation.helper.ApiHelper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Providers extends ProviderHolder<TotemOfPreserving> implements
    ITotemPreservingProvider,
    ITotemInventoryCheckProvider,
    IConditionalRecipeProvider,
    IConditionalAdvancementProvider
{
    public final List<ITotemPreservingProvider> preservingProviders = new ArrayList<>();
    public final List<ITotemInventoryCheckProvider> inventoryCheckProviders = new ArrayList<>();

    public Providers(TotemOfPreserving feature) {
        super(feature);
    }

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
            var totem = feature().registers.item.get();

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
    public List<IConditionalRecipe> getRecipeConditions() {
        return List.of(new IConditionalRecipe() {
            @Override
            public boolean test() {
                return !TotemOfPreserving.graveMode;
            }

            @Override
            public List<String> recipes() {
                return List.of(
                    "charm:totem_of_preserving/*"
                );
            }
        });
    }

    @Override
    public List<IConditionalAdvancement> getAdvancementConditions() {
        return List.of(new IConditionalAdvancement() {
            @Override
            public boolean test() {
                return !TotemOfPreserving.graveMode;
            }

            @Override
            public List<String> advancements() {
                return List.of(
                    "charm:totem_of_preserving/recipes/*"
                );
            }
        });
    }

    @Override
    public void onEnabled() {
        ApiHelper.consume(ITotemPreservingProvider.class, preservingProviders::add);
        ApiHelper.consume(ITotemInventoryCheckProvider.class, inventoryCheckProviders::add);
    }
}
