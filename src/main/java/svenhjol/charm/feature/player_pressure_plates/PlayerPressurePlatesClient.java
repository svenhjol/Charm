package svenhjol.charm.feature.player_pressure_plates;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

@ClientFeature(mod = CharmClient.MOD_ID, feature = PlayerPressurePlates.class)
public class PlayerPressurePlatesClient extends CharmonyFeature {
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
