package svenhjol.charm.feature.redstone_sand;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class RedstoneSandClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(RedstoneSand.class));
    }

    @Override
    public void runWhenEnabled() {
        CharmClient.instance().registry().itemTab(
            RedstoneSand.block,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.REDSTONE_BLOCK
        );
    }
}
