package svenhjol.charm.feature.storage_blocks.gunpowder;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony_api.iface.IStorageBlockFeature;

public class GunpowderClient implements IStorageBlockFeature {
    @Override
    public void runWhenEnabled() {
        Mods.client(Charm.ID).registry().itemTab(
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
