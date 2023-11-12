package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.feature.storage_blocks.ender_pearls.EnderPearlsClient;
import svenhjol.charm.feature.storage_blocks.gunpowder.GunpowderClient;
import svenhjol.charm.feature.storage_blocks.sugar.SugarClient;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.iface.IClientRegistry;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StorageBlocksClient extends ClientFeature {
    static Map<Class<? extends IStorageBlockFeature<IClientRegistry>>, IStorageBlockFeature<IClientRegistry>>
        LOADED_STORAGE_BLOCKS = new HashMap<>();

    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return StorageBlocks.class;
    }

    @Override
    public void register() {
        var registry = mod().registry();

        var features = List.of(
            EnderPearlsClient.class,
            GunpowderClient.class,
            SugarClient.class
        );

        for (var feature : features) {
            register(registry, feature);
        }
    }

    @Override
    public void runWhenEnabled() {
        LOADED_STORAGE_BLOCKS.values().forEach(block -> {
            if (block.isEnabled()) {
                block.runWhenEnabled();
            } else {
                block.runWhenDisabled();
            }
        });
    }

    @Override
    public void runWhenDisabled() {
        LOADED_STORAGE_BLOCKS.values().forEach(IStorageBlockFeature::runWhenDisabled);
    }

    public static void register(IClientRegistry registry, Class<? extends IStorageBlockFeature<IClientRegistry>> feature) {
        var log = registry.getLog();

        try {
            var instance = feature.getDeclaredConstructor().newInstance();
            instance.preRegister(registry);
            instance.register();
            LOADED_STORAGE_BLOCKS.put(feature, instance);
        } catch (Exception e) {
            log.warn(StorageBlocksClient.class, "Error loading client storage block " + feature + ", skipping: " + e.getMessage());
        }
    }
}
