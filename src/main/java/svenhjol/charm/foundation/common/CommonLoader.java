package svenhjol.charm.foundation.common;

import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.Loader;
import svenhjol.charm.foundation.Log;

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

        Resolve.COMMON_LOADERS.put(id, loader);
        return loader;
    }

    @Override
    protected Class<? extends Loader<CommonFeature>> type() {
        return CommonLoader.class;
    }

    @Override
    protected void configure() {
        sortFeaturesByName();

        config.readConfig(features);
        config.writeConfig(features);
    }

    public CommonRegistry registry() {
        return registry;
    }
}
