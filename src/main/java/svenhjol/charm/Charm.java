package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.handler.LogHandler;
import svenhjol.charm.loader.CommonLoader;
import svenhjol.charm.init.*;
import svenhjol.charm.loader.CommonModule;

@SuppressWarnings("unused")
public class Charm implements ModInitializer {
    public static final String MOD_ID = "charm";
    public static final LogHandler LOG = new LogHandler("Charm");
    public static CommonLoader<CommonModule> LOADER = new CommonLoader<>(MOD_ID, "svenhjol.charm.module");

    private static boolean hasStartedCharm = false;

    @Override
    public void onInitialize() {
        startCharm();
    }

    public static void startCharm() {
        if (hasStartedCharm) return;

        CharmLoot.init();
        CharmParticles.init();
        CharmStructures.init();
        CharmSounds.init();
        CharmTags.init();
        CharmDecorations.init();
        CharmBiomes.init();
        CharmAdvancements.init();

        LOADER.start();

        hasStartedCharm = true;
    }
}
