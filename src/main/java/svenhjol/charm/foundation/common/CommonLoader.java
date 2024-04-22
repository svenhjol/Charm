package svenhjol.charm.foundation.common;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Globals;
import svenhjol.charm.foundation.Loader;
import svenhjol.charm.foundation.Log;

import java.util.Comparator;

public class CommonLoader extends Loader<CommonFeature> {
    private final CommonConfig config;

    protected CommonLoader(String id, Log log, CommonConfig config) {
        super(id, log);
        this.config = config;
    }

    public static CommonLoader create(String id) {
        var log = new Log(id);
        var config = new CommonConfig(id, log);
        var loader = new CommonLoader(id, log, config);

        Globals.COMMON_LOADERS.put(id, loader);
        return loader;
    }

    @Override
    protected void configure() {
        // Sort features alphabetically for configuration.
        features.sort(Comparator.comparing(Feature::name));

        config.readConfig(features);
        config.writeConfig(features);
    }
}
