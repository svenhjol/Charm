package svenhjol.charm.fabric;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.Charm;

public class Initializer implements ModInitializer {
    @Override
    public void onInitialize() {
        Charm.init();
    }
}
