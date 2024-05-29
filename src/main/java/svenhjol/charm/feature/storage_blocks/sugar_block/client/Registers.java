package svenhjol.charm.feature.storage_blocks.sugar_block.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.storage_blocks.sugar_block.SugarBlockClient;

public final class Registers extends RegisterHolder<SugarBlockClient> {
    public Registers(SugarBlockClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().linked().registers.item,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.HONEY_BLOCK
        );
    }
}
