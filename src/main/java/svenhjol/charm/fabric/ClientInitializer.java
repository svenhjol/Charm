package svenhjol.charm.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.CharmClient;
import svenhjol.charm_core.CoreClient;
import svenhjol.charm_core.annotation.ClientFeature;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        // Init core client first.
        CoreClient.instance();

        var client = CharmClient.instance();
        var loader = client.loader();

        loader.init(client.featurePrefix(), ClientFeature.class);
        loader.run();
    }
}
