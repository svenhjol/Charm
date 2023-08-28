package svenhjol.charm.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.CharmClient;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CharmClient.init();
    }
}
