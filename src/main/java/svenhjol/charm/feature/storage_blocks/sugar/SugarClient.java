package svenhjol.charm.feature.storage_blocks.sugar;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony_api.iface.IStorageBlockFeature;

public class SugarClient implements IStorageBlockFeature {
    @Override
    public void runWhenEnabled() {
        Mods.client(Charm.ID).registry().itemTab(
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
