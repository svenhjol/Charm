package svenhjol.charm.foundation;

import svenhjol.charm.foundation.client.ClientLoader;
import svenhjol.charm.foundation.common.CommonLoader;
import svenhjol.charm.foundation.enums.Side;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Globals {
    public static final Map<String, CommonLoader> COMMON_LOADERS = new LinkedHashMap<>();
    public static final Map<String, ClientLoader> CLIENT_LOADERS = new LinkedHashMap<>();

    public static boolean has(Side side, String id) {
        return switch (side) {
            case COMMON -> COMMON_LOADERS.containsKey(id);
            case CLIENT -> CLIENT_LOADERS.containsKey(id);
            case SERVER -> false;
        };
    }
}
