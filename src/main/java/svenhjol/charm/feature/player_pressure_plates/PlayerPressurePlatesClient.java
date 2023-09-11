package svenhjol.charm.feature.player_pressure_plates;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class PlayerPressurePlatesClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(PlayerPressurePlates.class));
    }

    @Override
    public void runWhenEnabled() {
        var registry = CharmClient.instance().registry();

        // Add to the Building Blocks menu tab.
        registry.itemTab(
            PlayerPressurePlates.blockItem,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.GILDED_BLACKSTONE
        );
        // Add to the Redstone Blocks menu tab.
        registry.itemTab(
            PlayerPressurePlates.blockItem,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.HEAVY_WEIGHTED_PRESSURE_PLATE
        );
    }
}
