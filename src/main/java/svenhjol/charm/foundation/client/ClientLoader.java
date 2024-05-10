package svenhjol.charm.foundation.client;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Loader;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Resolve;

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

        Resolve.register(loader); // Make this loader available across all Charm mods.
        ClientEvents.runOnce(); // Safe to call multiple times; ensures global events are set up.

        return loader;
    }

    @Override
    protected Class<? extends Loader<ClientFeature>> type() {
        return ClientLoader.class;
    }

    /**
     * Update the enabled status of all client features to common features with the same name.
     */
    @Override
    protected void checks() {
        var commonLoader = Resolve.common(this.id());

        for (ClientFeature feature : features()) {
            var featureName = feature.name();
            if (commonLoader.has(featureName)) {
                // If there's a common feature matching the feature name,
                // match the enabled state of the client and common features.
                var commonFeatureIsEnabled = commonLoader.isEnabled(featureName);
                feature.setEnabled(feature.isEnabled() && commonFeatureIsEnabled);
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
