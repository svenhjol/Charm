package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.handler.LogHandler;
import svenhjol.charm.init.*;

public class Charm implements ModInitializer {
    public static final String MOD_ID = "charm";
    public static LogHandler LOG = new LogHandler("Charm");

    private static boolean hasCharmInit = false;

    @Override
    public void onInitialize() {
        initCharm();
    }

    /**
     * Use this to launch Charm submodules.
     */
    public static void init(String modId) {
        initCharm();
        new CharmLoader(modId);
    }

    private static void initCharm() {
        if (hasCharmInit)
            return;

        hasCharmInit = true;
        new CharmLoader(MOD_ID);

        CharmLoot.init();
        CharmParticles.init();
        CharmStructures.init();
        CharmSounds.init();
        CharmTags.init();
        CharmAdvancements.init();
    }
}
