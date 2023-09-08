package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.api.IStorageBlockFeature;
import svenhjol.charm.api.IStorageBlockProvider;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.ApiHelper;

import java.util.HashMap;
import java.util.Map;

@ClientFeature(mod = Charm.MOD_ID)
public class StorageBlocksClient extends CharmFeature {
    static Map<Class<? extends IStorageBlockFeature>, IStorageBlockFeature> LOADED_STORAGE_BLOCKS = new HashMap<>();

    @Override
    public void register() {
        var log = CharmClient.instance().log();

        ApiHelper.consume(IStorageBlockProvider.class,
            provider -> provider.getStorageBlockClientFeatures().forEach(
                clazz -> {
                    try {
                        var instance = (IStorageBlockFeature)clazz.getDeclaredConstructor().newInstance();
                        instance.preRegister();
                        instance.register();
                        LOADED_STORAGE_BLOCKS.put(clazz, instance);
                    } catch (Exception e) {
                        log.warn(getClass(), "Error loading client storage block " + clazz + ", skipping: " + e.getMessage());
                    }
                }));
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
}
