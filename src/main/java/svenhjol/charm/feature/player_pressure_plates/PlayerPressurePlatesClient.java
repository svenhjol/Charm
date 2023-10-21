package svenhjol.charm.feature.player_pressure_plates;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

public class PlayerPressurePlatesClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return PlayerPressurePlates.class;
    }

    @Override
    public void runWhenEnabled() {
        var registry = mod().registry();

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
