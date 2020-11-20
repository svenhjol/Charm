package svenhjol.charm;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.base.handler.ClientHandler;
import svenhjol.charm.base.handler.LogHandler;

public class CharmClient implements ClientModInitializer {
    public static LogHandler LOG = new LogHandler("CharmClient");

    @Override
    public void onInitializeClient() {
        ClientHandler.init();
    }
}
