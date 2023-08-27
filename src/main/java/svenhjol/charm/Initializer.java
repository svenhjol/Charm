package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm_core.CharmCore;

public class Initializer implements ModInitializer {
    @Override
    public void onInitialize() {
        Charm.init();
    }
}
