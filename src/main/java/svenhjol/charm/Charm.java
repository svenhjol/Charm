package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.handler.LogHandler;
import svenhjol.charm.init.*;

public class Charm implements ModInitializer {
    public static final String MOD_ID = "charm";
    public static LogHandler LOG = new LogHandler("Charm");

    private static boolean hasRunFirst = false;

    public static void runFirst() {
        if (hasRunFirst)
            return;

        new CharmLoader(MOD_ID);

        CharmLoot.init();
        CharmParticles.init();
        CharmStructures.init();
        CharmSounds.init();
        CharmTags.init();
        CharmAdvancements.init();

        hasRunFirst = true;
    }

    @Override
    public void onInitialize() {
        runFirst();
    }
}
