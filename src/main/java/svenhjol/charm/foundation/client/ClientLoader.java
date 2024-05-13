package svenhjol.charm.foundation.client;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Loader;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Resolve;
import svenhjol.charm.foundation.common.CommonResolver;

import java.util.Comparator;

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

    public static ClientLoader create(String id) {
        return Resolve.register(new ClientLoader(id));
    }

    @Override
    protected Class<? extends Loader<ClientFeature>> type() {
        return ClientLoader.class;
    }

    /**
     * Update client feature enabled status according to linked common feature's status.
     */
    @Override
    protected void checks() {
        for (ClientFeature feature : features()) {
            if (feature instanceof CommonResolver<?> resolver) {
                feature.setEnabled(resolver.common().isEnabled());
            }
        }

        super.checks();
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
