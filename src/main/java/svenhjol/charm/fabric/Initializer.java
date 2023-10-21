package svenhjol.charm.fabric;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.Charm;
import svenhjol.charmony.base.Mods;

public class Initializer implements ModInitializer {
    @Override
    public void onInitialize() {
        svenhjol.charmony.fabric.Initializer.initCharmony();

        var instance = Mods.common(Charm.ID, Charm::new);
        var loader = instance.loader();

        loader.init(instance.features());
        loader.run();
    }
}
