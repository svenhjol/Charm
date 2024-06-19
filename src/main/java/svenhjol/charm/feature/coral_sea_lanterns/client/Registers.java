package svenhjol.charm.feature.coral_sea_lanterns.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.coral_sea_lanterns.CoralSeaLanternsClient;

import java.util.ArrayList;
import java.util.Collections;

public final class Registers extends RegisterHolder<CoralSeaLanternsClient> {
    public Registers(CoralSeaLanternsClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();
        var lanterns = new ArrayList<>(feature().linked().registers.blockItems.values());
        Collections.reverse(lanterns);

        lanterns.forEach(supplier -> {
            registry.itemTab(supplier.get(), CreativeModeTabs.BUILDING_BLOCKS, Items.SEA_LANTERN);
            registry.itemTab(supplier.get(), CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.SEA_LANTERN);
        });
    }
}
