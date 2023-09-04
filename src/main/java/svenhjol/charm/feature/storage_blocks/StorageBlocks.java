package svenhjol.charm.feature.storage_blocks;

import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

import java.util.ArrayList;
import java.util.List;

@Feature(mod = Charm.MOD_ID, priority = -1)
public class StorageBlocks extends CharmFeature {
    private final static String PREFIX = "svenhjol.charm.feature.storage_blocks.blocks";

    public static List<String> blocks = List.of(
        "gunpowder",
        "sugar"
    );

    private static List<IStorageBlockFeature> cachedStorageBlocks = new ArrayList<>();

    @Override
    public void register() {
        // TODO: API.
    }

//    private List<IStorageBlockFeature> getStorageBlocks() {
//        if (cachedStorageBlocks.isEmpty()) {
//            var log = Charm.instance().log();
//
//            // Create the cache.
//            for (var name : blocks) {
//                var ucname = TextHelper.snakeToUpperCamel(name);
//
//                try {
//                    Class<?> clazz = Class.forName(PREFIX + "." + ucname);
//                    var instance = (IStorageBlockFeature)clazz.getDeclaredConstructor().newInstance();
//                    cachedStorageBlocks.add(instance);
//                } catch (Exception e) {
//                    log.warn("Skipping " + name + ": Could not create an instance. " + e.getMessage());
//                    continue;
//                }
//
//
//            }
//        }
//    }
}
