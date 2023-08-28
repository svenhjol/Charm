package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm_core.CharmCore;
import svenhjol.charm_core.Log;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmConfig;
import svenhjol.charm_core.base.CharmLoader;
import svenhjol.charm_core.common.CommonConfig;
import svenhjol.charm_core.common.CommonEvents;
import svenhjol.charm_core.common.CommonLoader;
import svenhjol.charm_core.common.CommonRegistry;
import svenhjol.charm_core.iface.ILog;
import svenhjol.charm_core.server.ServerNetwork;

public class Charm {
    public static Charm INSTANCE;
    public static final String MOD_ID = "charm";
    public static final String PREFIX = "svenhjol." + MOD_ID;
    public static final String FEATURE_PREFIX = PREFIX + ".feature";
    public static ILog LOG;
    public static CharmLoader LOADER;
    public static CommonRegistry REGISTRY;
    public static CommonEvents EVENTS;
    public static ServerNetwork NETWORK;
    public static CharmConfig CONFIG;

    public Charm() {
        LOG = new Log(MOD_ID);
        CONFIG = new CommonConfig(MOD_ID, LOG);
        REGISTRY = new CommonRegistry(MOD_ID, LOG);
        EVENTS = new CommonEvents();
        LOADER = new CommonLoader(MOD_ID, LOG, CONFIG);
        NETWORK = new ServerNetwork(LOG);

        // Autoload all annotated features from the feature namespace.
        LOADER.init(FEATURE_PREFIX, Feature.class);
    }

    public static void init() {
        // Start Core first.
        CharmCore.init();

        if (INSTANCE == null) {
            INSTANCE = new Charm();
            INSTANCE.run();
        }
    }

    public void run() {
        LOADER.run();
    }

    public static ResourceLocation makeId(String id) {
        return !id.contains(":") ? new ResourceLocation(MOD_ID, id) : new ResourceLocation(id);
    }
}
