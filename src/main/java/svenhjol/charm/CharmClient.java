package svenhjol.charm;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientEntityEvents;
import net.minecraft.client.player.LocalPlayer;
import svenhjol.charm.init.CharmClientParticles;
import svenhjol.charm.init.CharmDecorations;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.loader.ClientLoader;
import svenhjol.charm.loader.CommonLoader;

public class CharmClient implements ClientModInitializer {
    public static ClientLoader<CharmModule, CommonLoader<CharmModule>> LOADER = new ClientLoader<>(Charm.MOD_ID, Charm.LOADER, "svenhjol.charm.module");

    @Override
    public void onInitializeClient() {
        CharmClientParticles.init();

        LOADER.init();

        ClientEntityEvents.ENTITY_LOAD.register((entity, level)
            -> { if (entity instanceof LocalPlayer) CharmDecorations.init(); });
    }
}
