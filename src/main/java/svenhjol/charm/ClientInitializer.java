package svenhjol.charm;

import net.fabricmc.api.ClientModInitializer;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        CharmClient.init();
    }
}
