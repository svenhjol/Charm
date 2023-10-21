package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

import java.util.ArrayList;
import java.util.Collections;

public class CoralSeaLanternsClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return CoralSeaLanterns.class;
    }

    @Override
    public void runWhenEnabled() {
        var registry = mod().registry();
        var lanterns = new ArrayList<>(CoralSeaLanterns.BLOCK_ITEMS.values());
        Collections.reverse(lanterns);

        lanterns.forEach(value -> {
            registry.itemTab(value, CreativeModeTabs.BUILDING_BLOCKS, Items.SEA_LANTERN);
            registry.itemTab(value, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.SEA_LANTERN);
        });
    }
}
