package svenhjol.charm.charmony.common;

import svenhjol.charm.charmony.Config;

public class CommonConfig extends Config {
    public CommonConfig(CommonLoader loader) {
        super(loader.id() + "-common");
    }
}
