package svenhjol.charm.fabric;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.Charm;
import svenhjol.charm.foundation.common.CommonLoader;

public class CommonInitializer implements ModInitializer {
    private static boolean initialized = false;

    @Override
    public void onInitialize() {
        initCharm();
    }

    public static void initCharm() {
        if (initialized) return;

        var loader = CommonLoader.create(Charm.ID);
        loader.setup(Charm.features());
        loader.run();

        initialized = true;
    }
}
