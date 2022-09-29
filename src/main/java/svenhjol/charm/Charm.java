package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charm.helper.ConfigHelper;
import svenhjol.charm.init.*;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.loader.CommonLoader;
import svenhjol.charm.module.core.Core;

@SuppressWarnings("unused")
public class Charm implements ModInitializer {
    public static final String MOD_ID = "charm";
    public static final LogWrapper LOG = new LogWrapper(MOD_ID);
    public static final CommonLoader<CharmModule> LOADER = new CommonLoader<>(MOD_ID, "svenhjol.charm.module");

    private static boolean hasStartedCharm = false;

    @Override
    public void onInitialize() {
        init();
    }

    public static void init() {
        if (hasStartedCharm) return;

        CharmHacks.init();
        CharmResources.init();
        CharmParticles.init();
        CharmTags.init();
        CharmAdvancements.init();

        setupDebugMode();

        LOADER.init();

        hasStartedCharm = true;
    }

    private static void setupDebugMode() {
        var toml = ConfigHelper.readConfig(Charm.MOD_ID);
        Core.debug = ConfigHelper.isDebugMode(toml) || FabricLoader.getInstance().isDevelopmentEnvironment();
    }
}
