package svenhjol.charm.fabric;

import net.fabricmc.api.ClientModInitializer;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.base.Mods;

public class ClientInitializer implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        svenhjol.charmony.fabric.ClientInitializer.initCharmony();

        var instance = Mods.client(Charm.ID, CharmClient::new);
        var loader = instance.loader();

        loader.init(instance.features());
        loader.run();
    }
}
