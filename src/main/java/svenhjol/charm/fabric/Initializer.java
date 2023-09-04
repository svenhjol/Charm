package svenhjol.charm.fabric;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm_core.fabric.Charmony;

public class Initializer implements ModInitializer {
    @Override
    public void onInitialize() {
        Charmony.load("charm");
//        // Init core first.
//        Core.instance();
//
//        var charm = Charm.instance();
//        var loader = charm.loader();
//
//        // Autoload all annotated features from the feature namespace.
//        loader.init(charm.featurePrefix(), Feature.class);
//
//        // Launch all the features of this mod.
//        loader.run();
    }
}
