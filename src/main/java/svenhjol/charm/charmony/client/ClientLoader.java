package svenhjol.charm.charmony.client;

import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Loader;
import svenhjol.charm.charmony.Log;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.charmony.enums.Side;

import java.util.Comparator;
import java.util.Optional;

public class ClientLoader extends Loader<ClientFeature> {
    private final ClientConfig config;
    private final ClientRegistry registry;
    private final ClientEvents events;

    protected ClientLoader(String id) {
        super(id);
        this.log = new Log(id, this);
        this.config = new ClientConfig(this);
        this.registry = new ClientRegistry(this);
        this.events = new ClientEvents(this);
    }

    @Override
    public Side side() {
        return Side.CLIENT;
    }
    
    public Optional<ClientConfig> config() {
        return Optional.of(config);
    }

    public static ClientLoader create(String id) {
        return Resolve.register(new ClientLoader(id));
    }

    @Override
    protected Class<? extends Loader<ClientFeature>> type() {
        return ClientLoader.class;
    }

    @Override
    protected void configure() {
        // Sort features alphabetically for configuration.
        features.sort(Comparator.comparing(Feature::name));

        config.readConfig(features);
        config.writeConfig(features);
    }

    public ClientRegistry registry() {
        return registry;
    }
}
