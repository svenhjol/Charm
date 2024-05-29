package svenhjol.charm.charmony;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.charmony.enums.Side;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Resolving loaders and features.
 * To get a reference to a feature instance without a feature being passed, use the following options:
 * - make the class implement FeatureResolver and return the class type via typeForFeature()
 * - call the resolver directly using Resolve.feature()
 * Be careful when using mixins and FeatureResolver. Mixins that target the same class will have
 * inconsistent results when calling feature(). It's better to call the resolver directly
 * from the mixin method, e.g. Resolve.feature(SomeFeature.class).somemethod().
 */
@SuppressWarnings("unchecked")
public final class Resolve {
    private static final Map<Side, Map<String, Loader<?>>> LOADERS = new LinkedHashMap<>();
    private static final Map<Class<? extends Feature>, Feature> FEATURES = new LinkedHashMap<>();

    /**
     * Return true if a loader exists for the given side and modId.
     */
    public static boolean hasLoader(Side side, String id) {
        if (LOADERS.containsKey(side)) {
            var loaders = LOADERS.get(side);
            return loaders.containsKey(id);
        }
        return false;
    }

    /**
     * Return true if a feature exists and is enabled.
     */
    public static boolean isEnabled(Side side, ResourceLocation id) {
        var namespace = id.getNamespace();
        Loader<?> loader;

        if (LOADERS.containsKey(side)) {
            var loaders = LOADERS.get(side);
            loader = loaders.get(namespace);
        } else {
            loader = null;
        }

        if (loader == null) {
            return false;
        }

        return loader.isEnabled(id.getPath());
    }

    /**
     * Return true if a feature exists and is enabled.
     */
    public static <F extends Feature> boolean isEnabled(Class<F> clazz) {
        return tryFeature(clazz).map(Feature::isEnabled).orElse(false);
    }

    public static <F extends Feature> F feature(Class<F> clazz) {
        return tryFeature(clazz).orElseThrow(
            () -> new RuntimeException("Could not resolve feature for " + clazz));
    }

    public static <F extends Feature> F register(F feature) {
        var clazz = feature.getClass();
        FEATURES.put(clazz, feature);
        return feature;
    }

    public static <F extends Feature, L extends Loader<F>> L register(L loader) {
        var id = loader.id();
        var side = loader.side();

        LOADERS.computeIfAbsent(side, m -> new HashMap<>()).put(id, loader);
        return loader;
    }

    public static <F extends Feature> Optional<F> tryFeature(Class<F> clazz) {
        F resolved = (F) FEATURES.get(clazz);
        return Optional.ofNullable(resolved);
    }
}
