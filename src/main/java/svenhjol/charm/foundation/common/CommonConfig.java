package svenhjol.charm.foundation.common;

import svenhjol.charm.foundation.Config;

public class CommonConfig extends Config {
    public CommonConfig(CommonLoader loader) {
        super(loader.id() + "-common");
    }
}
