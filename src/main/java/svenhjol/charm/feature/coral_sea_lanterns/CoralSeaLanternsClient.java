package svenhjol.charm.feature.coral_sea_lanterns;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class CoralSeaLanternsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(CoralSeaLanterns.class));
    }

    @Override
    public void runWhenEnabled() {
        var registry = CharmClient.instance().registry();
        var values = new ArrayList<>(CoralSeaLanterns.BLOCK_ITEMS.values());
        Collections.reverse(values);

        values.forEach(value -> {
            registry.itemTab(value, CreativeModeTabs.BUILDING_BLOCKS, Items.SEA_LANTERN);
            registry.itemTab(value, CreativeModeTabs.FUNCTIONAL_BLOCKS, Items.SEA_LANTERN);
        });
    }
}
