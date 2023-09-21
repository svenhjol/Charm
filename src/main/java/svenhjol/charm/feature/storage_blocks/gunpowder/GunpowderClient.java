package svenhjol.charm.feature.storage_blocks.gunpowder;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmapi.iface.IStorageBlockFeature;

public class GunpowderClient implements IStorageBlockFeature {
    @Override
    public void runWhenEnabled() {
        CharmClient.instance().registry().itemTab(
            Gunpowder.item,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.HONEY_BLOCK
        );
    }

    @Override
    public boolean isEnabled() {
        return StorageBlocks.getStorageBlock(Gunpowder.class)
            .map(IStorageBlockFeature::isEnabled)
            .orElse(false);
    }
}
