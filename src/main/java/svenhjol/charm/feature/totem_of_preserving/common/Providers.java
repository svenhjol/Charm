package svenhjol.charm.feature.totem_of_preserving.common;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.api.enums.TotemType;
import svenhjol.charm.api.iface.*;
import svenhjol.charm.feature.totem_of_preserving.TotemOfPreserving;
import svenhjol.charm.foundation.feature.ProviderHolder;
import svenhjol.charm.foundation.Api;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public final class Providers extends ProviderHolder<TotemOfPreserving> implements
    TotemPreservingProvider,
    TotemInventoryCheckProvider,
    ConditionalRecipeProvider,
    ConditionalAdvancementProvider
{
    public final List<TotemPreservingProvider> preservingProviders = new ArrayList<>();
    public final List<TotemInventoryCheckProvider> inventoryCheckProviders = new ArrayList<>();

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
    public List<ConditionalRecipe> getRecipeConditions() {
        return List.of(new ConditionalRecipe() {
            @Override
            public boolean test() {
                return !feature().graveMode();
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
    public List<ConditionalAdvancement> getAdvancementConditions() {
        return List.of(new ConditionalAdvancement() {
            @Override
            public boolean test() {
                return !feature().graveMode();
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
        Api.consume(TotemPreservingProvider.class, preservingProviders::add);
        Api.consume(TotemInventoryCheckProvider.class, inventoryCheckProviders::add);
    }
}
