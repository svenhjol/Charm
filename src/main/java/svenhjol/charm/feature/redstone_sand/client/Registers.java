package svenhjol.charm.feature.redstone_sand.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.redstone_sand.RedstoneSandClient;

public final class Registers extends RegisterHolder<RedstoneSandClient> {
    public Registers(RedstoneSandClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().linked().registers.block.get(),
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.REDSTONE_BLOCK
        );
    }
}
