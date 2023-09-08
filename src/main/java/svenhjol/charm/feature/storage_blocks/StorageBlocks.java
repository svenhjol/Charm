package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.Charm;
import svenhjol.charm.api.IStorageBlockFeature;
import svenhjol.charm.api.IStorageBlockProvider;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.api.CharmonyApi;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charmony.helper.ApiHelper;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Feature(mod = Charm.MOD_ID, description = "More item blocks.")
public class StorageBlocks extends CharmFeature {
    static Map<Class<? extends IStorageBlockFeature>, IStorageBlockFeature> LOADED_STORAGE_BLOCKS = new HashMap<>();

    public static Optional<IStorageBlockFeature> getStorageBlock(Class<? extends IStorageBlockFeature> clazz) {
        return Optional.ofNullable(LOADED_STORAGE_BLOCKS.get(clazz));
    }

    @Configurable(name = "Ender pearls", description = "If true, ender pearl blocks will be enabled.")
    public static boolean enderPearls = true;

    @Configurable(name = "Gunpowder", description = "If true, gunpowder blocks will be enabled.")
    public static boolean gunpowder = true;

    @Configurable(name = "Sugar", description = "If true, sugar blocks will be enabled.")
    public static boolean sugar = true;

    @Override
    public void register() {
        var log = Charm.instance().log();

        ApiHelper.consume(IStorageBlockProvider.class,
            provider -> provider.getStorageBlockFeatures().forEach(
                clazz -> {
                    try {
                        var instance = (IStorageBlockFeature)clazz.getDeclaredConstructor().newInstance();
                        instance.preRegister();
                        instance.register();
                        LOADED_STORAGE_BLOCKS.put(clazz, instance);
                    } catch (Exception e) {
                        log.warn(getClass(), "Error loading storage block " + clazz + ", skipping: " + e.getMessage());
                    }
                }));

        CharmonyApi.registerProvider(new StorageBlockFeatures());
        CharmonyApi.registerProvider(new StorageBlockRecipeFilters());
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
