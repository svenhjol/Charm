package svenhjol.charm.foundation.common;

import svenhjol.charm.foundation.Config;
import svenhjol.charm.foundation.Log;

public class CommonConfig extends Config {
    public CommonConfig(String id, Log log) {
        super(id + "-common", log);
    }
}
