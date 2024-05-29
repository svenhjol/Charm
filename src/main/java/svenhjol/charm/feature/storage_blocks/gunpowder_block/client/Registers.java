package svenhjol.charm.feature.storage_blocks.gunpowder_block.client;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.GunpowderBlockClient;

public final class Registers extends RegisterHolder<GunpowderBlockClient> {
    public Registers(GunpowderBlockClient feature) {
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
