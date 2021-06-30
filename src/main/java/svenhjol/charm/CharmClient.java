package svenhjol.charm;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.handler.ClientModuleHandler;
import svenhjol.charm.handler.LogHandler;
import svenhjol.charm.init.CharmClientParticles;
import svenhjol.charm.init.CharmDecorations;

public class CharmClient implements ClientModInitializer {
    public static final String MOD_ID = "charm";
    public static LogHandler LOG = new LogHandler("CharmClient");

    @Override
    public void onInitializeClient() {
        ClientModuleHandler.INSTANCE.launch(MOD_ID);

        CharmClientParticles.init();
        CharmDecorations.init();
    }
}
