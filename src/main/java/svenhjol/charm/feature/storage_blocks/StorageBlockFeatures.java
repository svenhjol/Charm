package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.api.IStorageBlockFeature;
import svenhjol.charm.api.IStorageBlockProvider;
import svenhjol.charm.feature.storage_blocks.gunpowder.Gunpowder;
import svenhjol.charm.feature.storage_blocks.gunpowder.GunpowderClient;

import java.util.ArrayList;
import java.util.List;

public class StorageBlockFeatures implements IStorageBlockProvider {
    @Override
    public List<Class<? extends IStorageBlockFeature>> getStorageBlockFeatures() {
        List<Class<? extends IStorageBlockFeature>> out = new ArrayList<>();

        if (StorageBlocks.gunpowder) {
            out.add(Gunpowder.class);
        }

        return out;
    }

    @Override
    public List<Class<? extends IStorageBlockFeature>> getStorageBlockClientFeatures() {
        List<Class<? extends IStorageBlockFeature>> out = new ArrayList<>();

        if (StorageBlocks.gunpowder) {
            out.add(GunpowderClient.class);
        }

        return out;
    }
}
