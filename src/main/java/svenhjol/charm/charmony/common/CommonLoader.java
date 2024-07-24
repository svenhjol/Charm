package svenhjol.charm.charmony.common;

import svenhjol.charm.charmony.Loader;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.enums.Side;

import java.util.Optional;

public class CommonLoader extends Loader<CommonFeature> {
    private final CommonConfig config;
    private final CommonRegistry registry;
    private final CommonEvents events;

    protected CommonLoader(String id) {
        super(id);
        this.log = new Log(id, this);
        this.config = new CommonConfig(this);
        this.registry = new CommonRegistry(this);
        this.events = new CommonEvents(this);
    }

    @Override
    public Side side() {
        return Side.COMMON;
    }
    
    public Optional<CommonConfig> config() {
        return Optional.of(config);
    }

    public static CommonLoader create(String id) {
        return Resolve.register(new CommonLoader(id));
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
