package svenhjol.charm.feature.storage_blocks.sugar;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charm.api.IStorageBlockFeature;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;

public class SugarClient implements IStorageBlockFeature {
    @Override
    public void runWhenEnabled() {
        CharmClient.instance().registry().itemTab(
            Sugar.item,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.HONEY_BLOCK
        );
    }

    @Override
    public boolean isEnabled() {
        return StorageBlocks.getStorageBlock(Sugar.class)
            .map(IStorageBlockFeature::isEnabled)
            .orElse(false);
    }
}
