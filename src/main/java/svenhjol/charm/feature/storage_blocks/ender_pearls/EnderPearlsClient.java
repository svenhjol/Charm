package svenhjol.charm.feature.storage_blocks.ender_pearls;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.CharmClient;
import svenhjol.charm.api.IStorageBlockFeature;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;

public class EnderPearlsClient implements IStorageBlockFeature {
    @Override
    public void runWhenEnabled() {
        CharmClient.instance().registry().itemTab(
            EnderPearls.blockItem,
            CreativeModeTabs.FUNCTIONAL_BLOCKS,
            Items.ENDER_EYE
        );
    }

    @Override
    public boolean isEnabled() {
        return StorageBlocks.getStorageBlock(EnderPearls.class)
            .map(IStorageBlockFeature::isEnabled)
            .orElse(false);
    }
}
