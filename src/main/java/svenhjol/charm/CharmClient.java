package svenhjol.charm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.player.LocalPlayer;
import svenhjol.charm.handler.LogHandler;
import svenhjol.charm.init.CharmClientParticles;
import svenhjol.charm.init.CharmDecorations;
import svenhjol.charm.loader.ClientLoader;
import svenhjol.charm.loader.CharmModule;

public class CharmClient implements ClientModInitializer {
    public static final String MOD_ID = "charm";
    public static LogHandler LOG = new LogHandler("CharmClient");

    public static ClientLoader<CharmModule> LOADER = new ClientLoader<>(MOD_ID, "svenhjol.charm.module");

    @Override
    public void onInitializeClient() {
        CharmClientParticles.init();

        LOADER.init();

        ClientEntityEvents.ENTITY_LOAD.register((entity, level)
            -> { if (entity instanceof LocalPlayer) CharmDecorations.init(); });
    }
}
