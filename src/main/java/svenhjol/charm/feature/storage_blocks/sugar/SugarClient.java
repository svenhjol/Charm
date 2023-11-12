package svenhjol.charm.feature.storage_blocks.sugar;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.storage_blocks.IStorageBlockFeature;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.iface.IClientRegistry;

public class SugarClient implements IStorageBlockFeature<IClientRegistry> {
    IClientRegistry registry;

    @Override
    public void runWhenEnabled() {
        registry.itemTab(
            Sugar.item,
            CreativeModeTabs.REDSTONE_BLOCKS,
            Items.HONEY_BLOCK
        );
    }

    @Override
    public void preRegister(IClientRegistry registry) {
        this.registry = registry;
    }

    @Override
    public boolean isEnabled() {
        return StorageBlocks.getStorageBlock(Sugar.class)
            .map(IStorageBlockFeature::isEnabled)
            .orElse(false);
    }
}
