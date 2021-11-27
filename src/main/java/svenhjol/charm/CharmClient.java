package svenhjol.charm;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.init.CharmClientParticles;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.loader.ClientLoader;
import svenhjol.charm.loader.CommonLoader;

public class CharmClient implements ClientModInitializer {
    public static ClientLoader<CharmModule, CommonLoader<CharmModule>> LOADER = new ClientLoader<>(Charm.MOD_ID, Charm.LOADER, "svenhjol.charm.module");

    @Override
    public void onInitializeClient() {
        CharmClientParticles.init();
        LOADER.init();
    }
}
