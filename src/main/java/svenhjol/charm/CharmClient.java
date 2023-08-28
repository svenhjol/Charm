package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm_core.CoreClient;
import svenhjol.charm_core.Log;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmConfig;
import svenhjol.charm_core.base.CharmLoader;
import svenhjol.charm_core.client.*;
import svenhjol.charm_core.iface.ILog;

public class CharmClient {
    public static CharmClient INSTANCE;
    public static final String MOD_ID = Charm.MOD_ID;
    public static final String PREFIX = "svenhjol." + MOD_ID;
    public static final String FEATURE_PREFIX = PREFIX + ".feature";
    public static ILog LOG;
    public static CharmLoader LOADER;
    public static ClientEvents EVENTS;
    public static ClientRegistry REGISTRY;
    public static ClientNetwork NETWORK;
    public static CharmConfig CONFIG;

    public CharmClient() {
        // Initialize services.
        LOG = new Log(MOD_ID);
        CONFIG = new ClientConfig(MOD_ID, LOG);
        LOADER = new ClientLoader(MOD_ID, LOG, CONFIG);
        REGISTRY = new ClientRegistry(MOD_ID, LOG);
        EVENTS = new ClientEvents(REGISTRY);
        NETWORK = new ClientNetwork(LOG);

        // Autoload all annotated client features from the feature namespace.
        LOADER.init(FEATURE_PREFIX, ClientFeature.class);
    }

    public static void init() {
        // Start Core first.
        CoreClient.init();

        if (INSTANCE == null) {
            INSTANCE = new CharmClient();
            INSTANCE.run();
        }
    }

    public static ResourceLocation makeId(String id) {
        return !id.contains(":") ? new ResourceLocation(MOD_ID, id) : new ResourceLocation(id);
    }

    public void run() {
        LOADER.run();
    }
}
