package svenhjol.charm.feature.wood;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmFeature;

import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;

@ClientFeature(priority = -10)
public class WoodClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        // TODO: check should this be CharmClient?
        return List.of(()
            -> Charm.instance().loader().isEnabled(Wood.class));
    }

    @Override
    public void register() {
        var registry = CharmClient.instance().registry();

        if (isEnabled()) {
            // TODO: constants instead of magic strings.
            List<String> buildingBlocks = List.of(
                "button", "pressure_plate", "trapdoor", "door",
                "fence_gate", "fence", "slab", "stairs", "planks",
                "stripped_wood", "stripped_log", "wood", "log"
            );

            for (var modId : Wood.CREATIVE_TAB_ITEMS.keySet()) {
                var items = Wood.CREATIVE_TAB_ITEMS.get(modId);

                for (String name : buildingBlocks) {
                    Optional.ofNullable(items.get(name)).ifPresent(
                        item -> registry.itemTab(item, CreativeModeTabs.BUILDING_BLOCKS, Items.ACACIA_BUTTON)
                    );
                }

                Optional.ofNullable(items.get("leaves")).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_LEAVES)
                );

                Optional.ofNullable(items.get("log")).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_LOG)
                );

                Optional.ofNullable(items.get("sapling")).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.NATURAL_BLOCKS, Items.ACACIA_SAPLING)
                );

                Optional.ofNullable(items.get("sign")).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.ACACIA_SIGN)
                );

                Optional.ofNullable(items.get("hanging_sign")).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.ACACIA_HANGING_SIGN)
                );

                Optional.ofNullable(items.get("chest_boat")).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.ACACIA_CHEST_BOAT)
                );

                Optional.ofNullable(items.get("boat")).ifPresent(
                    item -> registry.itemTab(item, CreativeModeTabs.TOOLS_AND_UTILITIES, Items.ACACIA_CHEST_BOAT)
                );
            }
        }
    }
}
