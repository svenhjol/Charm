package svenhjol.charm.feature.player_pressure_plates.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.player_pressure_plates.PlayerPressurePlatesClient;
import svenhjol.charm.foundation.feature.RegisterHolder;

public final class Registers extends RegisterHolder<PlayerPressurePlatesClient> {
    public Registers(PlayerPressurePlatesClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();

        // Add to the Building Blocks menu tab.
        registry.itemTab(
            feature().common().registers.blockItem,
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.SMOOTH_BASALT
        );
        // Add to the Redstone Blocks menu tab.
        registry.itemTab(
            feature().common().registers.blockItem,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.HEAVY_WEIGHTED_PRESSURE_PLATE
        );
    }
}
