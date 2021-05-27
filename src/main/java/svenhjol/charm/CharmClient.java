package svenhjol.charm;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.init.CharmClientLoader;
import svenhjol.charm.handler.LogHandler;
import svenhjol.charm.init.CharmClientParticles;

public class CharmClient implements ClientModInitializer {
    public static LogHandler LOG = new LogHandler("CharmClient");

    @Override
    public void onInitializeClient() {
        new CharmClientLoader(Charm.MOD_ID);

        CharmClientParticles.init();
    }
}
