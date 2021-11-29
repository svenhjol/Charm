package svenhjol.charm.module.husks_spawn_underground;

import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Husks spawn anywhere within their biome rather than just the surface.")
public class HusksSpawnUnderground extends CharmModule {
    public static boolean canSpawn() {
        return Charm.LOADER.isEnabled(HusksSpawnUnderground.class);
    }
}
