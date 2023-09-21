package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.feature.storage_blocks.ender_pearls.EnderPearls;
import svenhjol.charm.feature.storage_blocks.ender_pearls.EnderPearlsClient;
import svenhjol.charm.feature.storage_blocks.gunpowder.Gunpowder;
import svenhjol.charm.feature.storage_blocks.gunpowder.GunpowderClient;
import svenhjol.charm.feature.storage_blocks.sugar.Sugar;
import svenhjol.charm.feature.storage_blocks.sugar.SugarClient;
import svenhjol.charmapi.iface.IStorageBlockFeature;
import svenhjol.charmapi.iface.IStorageBlockProvider;

import java.util.List;

public class StorageBlockFeatures implements IStorageBlockProvider {
    @Override
    public List<Class<? extends IStorageBlockFeature>> getStorageBlockFeatures() {
        return List.of(
            EnderPearls.class,
            Gunpowder.class,
            Sugar.class
        );
    }

    @Override
    public List<Class<? extends IStorageBlockFeature>> getStorageBlockClientFeatures() {
        return List.of(
            EnderPearlsClient.class,
            GunpowderClient.class,
            SugarClient.class
        );
    }
}
