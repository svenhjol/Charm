package svenhjol.charm.feature.storage_blocks.ender_pearls;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony_api.iface.IStorageBlockFeature;

public class EnderPearlsClient implements IStorageBlockFeature {
    @Override
    public void runWhenEnabled() {
        Mods.client(Charm.ID).registry().itemTab(
            EnderPearls.item,
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
