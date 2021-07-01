package svenhjol.charm;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.loader.ClientLoader;
import svenhjol.charm.handler.LogHandler;
import svenhjol.charm.init.CharmClientParticles;
import svenhjol.charm.init.CharmDecorations;
import svenhjol.charm.loader.ClientModule;
import svenhjol.charm.loader.CommonModule;

public class CharmClient implements ClientModInitializer {
    public static final String MOD_ID = "charm";
    public static LogHandler LOG = new LogHandler("CharmClient");

    public static ClientLoader<ClientModule, CommonModule> LOADER;

    @Override
    public void onInitializeClient() {
        LOADER = new ClientLoader<>(Charm.LOADER, MOD_ID, "svenhjol.charm.module");

        LOADER.run();

        CharmClientParticles.init();
        CharmDecorations.init();
    }
}
