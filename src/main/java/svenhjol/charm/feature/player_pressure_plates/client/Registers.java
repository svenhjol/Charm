package svenhjol.charm.feature.player_pressure_plates.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.player_pressure_plates.PlayerPressurePlatesClient;

public final class Registers extends RegisterHolder<PlayerPressurePlatesClient> {
    public Registers(PlayerPressurePlatesClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();

        // Add to the Building Blocks menu tab.
        registry.itemTab(
            feature().linked().registers.blockItem.get(),
            CreativeModeTabs.BUILDING_BLOCKS,
            Items.SMOOTH_BASALT
        );
        // Add to the Redstone Blocks menu tab.
        registry.itemTab(
            feature().linked().registers.blockItem.get(),
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.HEAVY_WEIGHTED_PRESSURE_PLATE
        );
    }
}
