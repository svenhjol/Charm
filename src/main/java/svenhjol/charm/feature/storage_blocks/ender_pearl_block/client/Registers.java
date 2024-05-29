package svenhjol.charm.feature.storage_blocks.ender_pearl_block.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.storage_blocks.ender_pearl_block.EnderPearlBlockClient;

public final class Registers extends RegisterHolder<EnderPearlBlockClient> {
    public Registers(EnderPearlBlockClient feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        feature().registry().itemTab(
            feature().common().registers.item,
            CreativeModeTabs.FUNCTIONAL_BLOCKS,
            Items.ENDER_EYE
        );
    }
}
