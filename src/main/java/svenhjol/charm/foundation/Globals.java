package svenhjol.charm.foundation;

import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.enums.Side;
import svenhjol.charm.foundation.server.ServerLoader;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Globals {
    public static final Map<String, CommonLoader> COMMON_LOADERS = new LinkedHashMap<>();
    public static final Map<String, ClientLoader> CLIENT_LOADERS = new LinkedHashMap<>();
    public static final Map<String, ServerLoader> SERVER_LOADERS = new LinkedHashMap<>();

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
}
