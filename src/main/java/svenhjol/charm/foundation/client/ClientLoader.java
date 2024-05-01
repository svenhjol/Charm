package svenhjol.charm.foundation.client;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Globals;
import svenhjol.charm.foundation.Loader;
import svenhjol.charm.foundation.Log;

import java.util.Comparator;

public class ClientLoader extends Loader<ClientFeature> {
    private final ClientConfig config;
    private final ClientRegistry registry;
    private final ClientEvents events;

    protected ClientLoader(String id, ClientConfig config, ClientRegistry registry, ClientEvents events) {
        super(id);
        this.log = new Log(id, this);
        this.config = config;
        this.registry = registry;
        this.events = events;
    }

    public static ClientLoader create(String id) {
        var config = new ClientConfig(id);
        var registry = new ClientRegistry(id);
        var events = new ClientEvents(registry);
        var loader = new ClientLoader(id, config, registry, events);

        ClientEvents.runOnce(); // Safe to call multiple times; ensures global events are set up.

        Globals.CLIENT_LOADERS.put(id, loader);
        return loader;
    }

    @Override
    protected boolean featureInit(ClientFeature feature) {
        super.featureInit(feature); // Ensure that loader gets added to the feature.

        // If the associated common feature is disabled, also disable this client feature.
        var clazz = feature.commonClass();
        var loader = Globals.COMMON_LOADERS.get(feature.modId());
        if (clazz != null && loader != null) {
            loader.get(clazz).ifPresent(common -> feature.setEnabled(common.isEnabled()));
        }

        return true;
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
