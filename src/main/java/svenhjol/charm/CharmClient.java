package svenhjol.charm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.player.LocalPlayer;
import svenhjol.charm.loader.ClientLoader;
import svenhjol.charm.handler.LogHandler;
import svenhjol.charm.init.CharmClientParticles;
import svenhjol.charm.init.CharmDecorations;
import svenhjol.charm.loader.CharmClientModule;
import svenhjol.charm.loader.CharmCommonModule;

public class CharmClient implements ClientModInitializer {
    public static final String MOD_ID = "charm";
    public static LogHandler LOG = new LogHandler("CharmClient");

    public static ClientLoader<CharmClientModule, CharmCommonModule> LOADER = new ClientLoader<>(Charm.LOADER, MOD_ID, "svenhjol.charm.module");

    @Override
    public void onInitializeClient() {
        CharmClientParticles.init();

        LOADER.init();

        ClientEntityEvents.ENTITY_LOAD.register((entity, level)
            -> { if (entity instanceof LocalPlayer) CharmDecorations.init(); });
    }
}
