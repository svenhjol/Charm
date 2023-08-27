package svenhjol.charm;

import net.fabricmc.api.ClientModInitializer;

public class ClientInitalizer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CharmClient.init();
    }
}
