package svenhjol.charm.foundation;

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
import java.util.function.Supplier;

@SuppressWarnings("unchecked")
public final class Resolve {
    private static final Log LOGGER = new Log(Charm.ID, "Resolve");

    public static final Map<String, CommonLoader> COMMON_LOADERS = new LinkedHashMap<>();
    public static final Map<String, ClientLoader> CLIENT_LOADERS = new LinkedHashMap<>();
    public static final Map<String, ServerLoader> SERVER_LOADERS = new LinkedHashMap<>();

    public static final Map<Class<? extends Feature>, CommonFeature> COMMON_FEATURES = new LinkedHashMap<>();
    public static final Map<Class<? extends Feature>, ClientFeature> CLIENT_FEATURES = new LinkedHashMap<>();
    public static final Map<Class<? extends Feature>, ServerFeature> SERVER_FEATURES = new LinkedHashMap<>();

    public static boolean has(Side side, String id) {
        return switch (side) {
            case COMMON -> COMMON_LOADERS.containsKey(id);
            case CLIENT -> CLIENT_LOADERS.containsKey(id);
            case SERVER -> SERVER_LOADERS.containsKey(id);
        };
    }

    public static CommonLoader common(String id) {
        if (!has(Side.COMMON, id)) {
            throw new RuntimeException("No global common loader for mod " + id);
        }
        return COMMON_LOADERS.get(id);
    }

    public static ClientLoader client(String id) {
        if (!has(Side.COMMON, id)) {
            throw new RuntimeException("No global client loader for mod " + id);
        }
        return CLIENT_LOADERS.get(id);
    }

    public static ServerLoader server(String id) {
        if (!has(Side.SERVER, id)) {
            throw new RuntimeException("No global server loader for mod " + id);
        }
        return SERVER_LOADERS.get(id);
    }

    public static <F extends Feature> F feature(Class<F> clazz) {
        var supertype = clazz.getSuperclass();
        F resolved;

        if (supertype.equals(CommonFeature.class)) {
            resolved = (F) COMMON_FEATURES.get(clazz);
        } else if (supertype.equals(ClientFeature.class)) {
            resolved = (F) CLIENT_FEATURES.get(clazz);
        } else if (supertype.equals(ServerFeature.class)) {
            resolved = (F) SERVER_FEATURES.get(clazz);
        } else {
            throw new RuntimeException("Could not determine supertype for " + clazz);
        }

        if (resolved == null) {
            throw new RuntimeException("Could not resolve feature for " + clazz);
        }

        return resolved;
    }

    public static <F extends Feature> Supplier<F> defer(Class<F> clazz) {
        return () -> Resolve.feature(clazz);
    }

    public static <F extends Feature> void register(F feature) {
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
    }
}
