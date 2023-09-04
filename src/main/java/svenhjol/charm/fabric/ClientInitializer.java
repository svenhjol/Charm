package svenhjol.charm.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm_core.fabric.Charmony;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        Charmony.loadClient("charm");
    }
}
