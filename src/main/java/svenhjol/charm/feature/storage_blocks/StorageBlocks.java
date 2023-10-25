package svenhjol.charm.feature.storage_blocks;

import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.helper.ApiHelper;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.iface.IStorageBlockFeature;
import svenhjol.charmony_api.iface.IStorageBlockProvider;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class StorageBlocks extends CommonFeature {
    static final Map<Class<? extends IStorageBlockFeature>, IStorageBlockFeature> LOADED_STORAGE_BLOCKS = new HashMap<>();

    @Override
    public String description() {
        return "More item blocks.";
    }

    public static Optional<IStorageBlockFeature> getStorageBlock(Class<? extends IStorageBlockFeature> clazz) {
        return Optional.ofNullable(LOADED_STORAGE_BLOCKS.get(clazz));
    }

    @Configurable(name = "Ender pearls",
        description = "If true, ender pearl blocks will be enabled.")
    public static boolean enderPearlsEnabled = true;

    @Configurable(name = "Gunpowder",
        description = "If true, gunpowder blocks will be enabled.")
    public static boolean gunpowderEnabled = true;

    @Configurable(name = "Sugar",
        description = "If true, sugar blocks will be enabled.")
    public static boolean sugarEnabled = true;

    @Configurable(name = "Ender pearl block converts silverfish",
        description = "If true, ender pearl blocks will convert silverfish to endermites.")
    public static boolean enderPearlBlocksConvertSilverfish = true;


    @Override
    public void register() {
        var log = mod().log();

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

    @SuppressWarnings("unused")
    public boolean isEnderPearlsEnabled() {
        return this.isEnabled() && enderPearlsEnabled;
    }

    public boolean isGunpowderEnabled() {
        return this.isEnabled() && gunpowderEnabled;
    }

    @SuppressWarnings("unused")
    public boolean isSugarEnabled() {
        return this.isEnabled() && sugarEnabled;
    }

    @Override
    public void runWhenDisabled() {
        LOADED_STORAGE_BLOCKS.values().forEach(IStorageBlockFeature::runWhenDisabled);
    }
}
