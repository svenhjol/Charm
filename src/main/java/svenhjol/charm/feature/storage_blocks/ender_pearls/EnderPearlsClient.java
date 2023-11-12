package svenhjol.charm.feature.storage_blocks.ender_pearls;

import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.storage_blocks.IStorageBlockFeature;
import svenhjol.charm.feature.storage_blocks.StorageBlocks;
import svenhjol.charmony.base.Mods;
import svenhjol.charmony.iface.IClientRegistry;

public class EnderPearlsClient implements IStorageBlockFeature<IClientRegistry> {
    IClientRegistry registry;

    @Override
    public void preRegister(IClientRegistry registry) {
        this.registry = registry;
    }

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
