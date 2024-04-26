package svenhjol.charm.foundation.common;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Globals;
import svenhjol.charm.foundation.Loader;
import svenhjol.charm.foundation.Log;

import java.util.Comparator;

public class CommonLoader extends Loader<CommonFeature> {
    private final CommonConfig config;
    private final CommonRegistry registry;
    private final CommonEvents events;

    protected CommonLoader(String id, CommonConfig config, CommonRegistry registry, CommonEvents events) {
        super(id);
        this.log = new Log(id, this);
        this.config = config;
        this.registry = registry;
        this.events = events;
    }

    public static CommonLoader create(String id) {
        var config = new CommonConfig(id);
        var registry = new CommonRegistry(id);
        var events = new CommonEvents(registry);
        var loader = new CommonLoader(id, config, registry, events);

        CommonEvents.runOnce(); // Safe to call multiple times; ensures global events are set up.

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

    public CommonRegistry registry() {
        return registry;
    }
}
