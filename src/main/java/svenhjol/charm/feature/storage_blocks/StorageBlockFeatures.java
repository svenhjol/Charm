package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.api.IStorageBlockFeature;
import svenhjol.charm.api.IStorageBlockProvider;
import svenhjol.charm.feature.storage_blocks.ender_pearls.EnderPearls;
import svenhjol.charm.feature.storage_blocks.ender_pearls.EnderPearlsClient;
import svenhjol.charm.feature.storage_blocks.gunpowder.Gunpowder;
import svenhjol.charm.feature.storage_blocks.gunpowder.GunpowderClient;

import java.util.List;

public class StorageBlockFeatures implements IStorageBlockProvider {
    @Override
    public List<Class<? extends IStorageBlockFeature>> getStorageBlockFeatures() {
        return List.of(
            EnderPearls.class,
            Gunpowder.class
        );
    }

    @Override
    public List<Class<? extends IStorageBlockFeature>> getStorageBlockClientFeatures() {
        return List.of(
            EnderPearlsClient.class,
            GunpowderClient.class
        );
    }
}
