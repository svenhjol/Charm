package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.feature.storage_blocks.ender_pearls.EnderPearls;
import svenhjol.charm.feature.storage_blocks.gunpowder.Gunpowder;
import svenhjol.charm.feature.storage_blocks.sugar.Sugar;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.common.CommonFeature;
import svenhjol.charmony.iface.ICommonRegistry;
import svenhjol.charmony_api.CharmonyApi;

import java.util.HashMap;
import java.util.List;
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
        var registry = mod().registry();

        var features = List.of(
            EnderPearls.class,
            Gunpowder.class,
            Sugar.class
        );

        for (var feature : features) {
            register(registry, feature);
        }

        CharmonyApi.registerProvider(new StorageBlockDataProviders());
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

    public static void register(ICommonRegistry registry, Class<? extends IStorageBlockFeature<ICommonRegistry>> feature) {
        var log = registry.getLog();

        try {
            var instance = feature.getDeclaredConstructor().newInstance();
            instance.preRegister(registry);
            instance.register();
            LOADED_STORAGE_BLOCKS.put(feature, instance);
        } catch (Exception e) {
            log.warn(StorageBlocks.class, "Error loading storage block " + feature + ", skipping: " + e.getMessage());
        }
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
