package svenhjol.charm.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.foundation.client.ClientLoader;

public class ClientInitializer implements ClientModInitializer {
    private static boolean initialized = false;

    @Override
    public void onInitializeClient() {
        initCharm();
    }

    public static void initCharm() {
        if (initialized) return;

        var loader = ClientLoader.create(Charm.ID);
        loader.setup(CharmClient.features());
        loader.run();

        initialized = true;
    }
}
