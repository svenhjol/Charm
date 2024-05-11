package svenhjol.charm.foundation;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.foundation.client.ClientFeature;
import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.enums.Side;
import svenhjol.charm.foundation.server.ServerFeature;
import svenhjol.charm.foundation.server.ServerLoader;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Supplier;

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
    private static final Log LOGGER = new Log(Charm.ID, "Resolve");

    private static final Map<String, CommonLoader> COMMON_LOADERS = new LinkedHashMap<>();
    private static final Map<String, ClientLoader> CLIENT_LOADERS = new LinkedHashMap<>();
    private static final Map<String, ServerLoader> SERVER_LOADERS = new LinkedHashMap<>();

    private static final Map<Class<? extends Feature>, CommonFeature> COMMON_FEATURES = new LinkedHashMap<>();
    private static final Map<Class<? extends Feature>, ClientFeature> CLIENT_FEATURES = new LinkedHashMap<>();
    private static final Map<Class<? extends Feature>, ServerFeature> SERVER_FEATURES = new LinkedHashMap<>();

    public static boolean hasLoader(Side side, String id) {
        return switch (side) {
            case COMMON -> COMMON_LOADERS.containsKey(id);
            case CLIENT -> CLIENT_LOADERS.containsKey(id);
            case SERVER -> SERVER_LOADERS.containsKey(id);
        };
    }

    public static CommonLoader common(String id) {
        return Optional.ofNullable(COMMON_LOADERS.get(id)).orElseThrow(
            () -> new RuntimeException("No registered common loader for mod " + id));
    }

    public static ClientLoader client(String id) {
        return Optional.ofNullable(CLIENT_LOADERS.get(id)).orElseThrow(
            () -> new RuntimeException("No registered client loader for mod " + id));
    }

    public static ServerLoader server(String id) {
        return Optional.ofNullable(SERVER_LOADERS.get(id)).orElseThrow(
            () -> new RuntimeException("No registered server loader for mod " + id));
    }

    /**
     * Return true if a feature exists and is enabled.
     */
    public static boolean featureEnabled(Side side, ResourceLocation id) {
        var namespace = id.getNamespace();

        var loader = switch (side) {
            case COMMON -> COMMON_LOADERS.get(namespace);
            case CLIENT -> CLIENT_LOADERS.get(namespace);
            case SERVER -> SERVER_LOADERS.get(namespace);
        };

        if (loader == null) {
            return false;
        }

        return loader.isEnabled(id.getPath());
    }

    /**
     * Return true if a feature exists and is enabled.
     */
    public static <F extends Feature> boolean featureEnabled(Class<F> clazz) {
        return tryGetFeature(clazz).map(Feature::isEnabled).orElse(false);
    }

    public static <F extends Feature> F feature(Class<F> clazz) {
        return tryGetFeature(clazz).orElseThrow(
            () -> new RuntimeException("Could not resolve feature for " + clazz));
    }

    public static <F extends Feature> Supplier<F> defer(Class<F> clazz) {
        return () -> Resolve.feature(clazz);
    }

    public static <F extends Feature> F register(F feature) {
        var clazz = feature.getClass();
        var supertype = clazz.getSuperclass();

        if (supertype.equals(CommonFeature.class)) {
            COMMON_FEATURES.put(clazz, (CommonFeature) feature);
        } else if (supertype.equals(ClientFeature.class)) {
            CLIENT_FEATURES.put(clazz, (ClientFeature) feature);
        } else if (supertype.equals(ServerFeature.class)) {
            SERVER_FEATURES.put(clazz, (ServerFeature) feature);
        } else {
            throw new RuntimeException("Could not determine supertype for " + clazz);
        }

        return feature;
    }

    public static <F extends Feature, L extends Loader<F>> L register(L loader) {
        var id = loader.id();
        var clazz = loader.getClass();

        if (clazz.equals(CommonLoader.class)) {
            COMMON_LOADERS.put(id, (CommonLoader) loader);
        } else if (clazz.equals(ClientLoader.class)) {
            CLIENT_LOADERS.put(id, (ClientLoader) loader);
        } else if (clazz.equals(ServerLoader.class)) {
            SERVER_LOADERS.put(id, (ServerLoader) loader);
        } else {
            throw new RuntimeException("Could not determine class for " + clazz);
        }

        return loader;
    }

    private static <F extends Feature> Optional<F> tryGetFeature(Class<F> clazz) {
        var supertype = clazz.getSuperclass();
        F resolved;

        if (supertype.equals(CommonFeature.class)) {
            if (!COMMON_FEATURES.containsKey(clazz)) {
                return Optional.empty();
            }
            resolved = (F) COMMON_FEATURES.get(clazz);
        } else if (supertype.equals(ClientFeature.class)) {
            if (!CLIENT_FEATURES.containsKey(clazz)) {
                return Optional.empty();
            }
            resolved = (F) CLIENT_FEATURES.get(clazz);
        } else if (supertype.equals(ServerFeature.class)) {
            if (!SERVER_FEATURES.containsKey(clazz)) {
                return Optional.empty();
            }
            resolved = (F) SERVER_FEATURES.get(clazz);
        } else {
            throw new RuntimeException("Could not determine supertype for " + clazz);
        }

        return Optional.of(resolved);
    }
}
