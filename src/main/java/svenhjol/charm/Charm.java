package svenhjol.charm;

import net.fabricmc.api.ModInitializer;
import svenhjol.charm.handler.LogHandler;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.init.*;

@SuppressWarnings("unused")
public class Charm implements ModInitializer {
    public static final String MOD_ID = "charm";
    public static LogHandler LOG = new LogHandler("Charm");
    private static boolean hasInitializedCharm = false;

    @Override
    public void onInitialize() {
        initCharm();
    }

    /**
     * Use this to launch Charm submodules.
     */
    public static void initSubmodule(String modId) {
        initCharm();
        ModuleHandler.INSTANCE.launch(modId);
    }

    private static void initCharm() {
        if (hasInitializedCharm) return;
        ModuleHandler.INSTANCE.launch(MOD_ID);

        CharmLoot.init();
        CharmParticles.init();
        CharmStructures.init();
        CharmSounds.init();
        CharmTags.init();
        CharmDecorations.init();
        CharmAdvancements.init();

        hasInitializedCharm = true;
    }
}
