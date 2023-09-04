package svenhjol.charm.fabric;

import net.fabricmc.api.ModInitializer;
import svenhjol.charmony.fabric.CharmonyModLoader;

public class Initializer implements ModInitializer {
    @Override
    public void onInitialize() {
        CharmonyModLoader.mods("charm");
    }
}
