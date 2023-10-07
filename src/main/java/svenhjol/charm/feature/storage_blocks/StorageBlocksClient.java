package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.helper.ApiHelper;
import svenhjol.charmony_api.iface.IStorageBlockFeature;
import svenhjol.charmony_api.iface.IStorageBlockProvider;

import java.util.HashMap;
import java.util.Map;

@ClientFeature(mod = CharmClient.MOD_ID, feature = StorageBlocks.class)
public class StorageBlocksClient extends CharmonyFeature {
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
