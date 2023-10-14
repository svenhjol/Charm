package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

import java.util.ArrayList;
import java.util.Collections;

@ClientFeature(mod = CharmClient.MOD_ID, feature = CoralSeaLanterns.class)
public class CoralSeaLanternsClient extends CharmonyFeature {
    @Override
    public void runWhenEnabled() {
        var registry = CharmClient.instance().registry();
        var lanterns = new ArrayList<>(CoralSeaLanterns.BLOCK_ITEMS.values());
        Collections.reverse(lanterns);

        lanterns.forEach(value -> {
            registry.itemTab(value, CreativeModeTabs.BUILDING_BLOCKS, Items.SEA_LANTERN);
            registry.itemTab(value, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.SEA_LANTERN);
        });
    }
}
