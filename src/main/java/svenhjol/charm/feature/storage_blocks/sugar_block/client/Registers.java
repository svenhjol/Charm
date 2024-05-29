package svenhjol.charm.feature.storage_blocks.sugar_block.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.storage_blocks.sugar_block.SugarBlockClient;
import svenhjol.charm.charmony.feature.RegisterHolder;

public final class Registers extends RegisterHolder<SugarBlockClient> {
    public Registers(SugarBlockClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().common().registers.item,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.HONEY_BLOCK
        );
    }
}
