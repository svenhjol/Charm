package svenhjol.charm;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm_core.CharmCoreClient;
import svenhjol.charm_core.Log;
import svenhjol.charm_core.annotation.ClientFeature;
import svenhjol.charm_core.base.CharmConfig;
import svenhjol.charm_core.base.CharmLoader;
import svenhjol.charm_core.client.*;
import svenhjol.charm_core.iface.*;

public class CharmClient {
    private static CharmClient INSTANCE;
    public static final String MOD_ID = "charm";
    public static final String PREFIX = "svenhjol." + MOD_ID;
    public static final String FEATURE_PREFIX = PREFIX + ".feature";
    public static ILog LOG;
    public static CharmLoader LOADER;
    public static ClientEvents EVENTS;
    public static ClientRegistry REGISTRY;
    public static ClientNetwork NETWORK;
    public static CharmConfig CONFIG;

    public CharmClient() {
        LOG = new Log(MOD_ID);
        CONFIG = new ClientConfig(MOD_ID, LOG);
        LOADER = new ClientLoader(MOD_ID, LOG, CONFIG);
        REGISTRY = new ClientRegistry(MOD_ID, LOG);
        EVENTS = new ClientEvents(REGISTRY);
        NETWORK = new ClientNetwork();

        // Autoload all annotated client features from the feature namespace.
        LOADER.init(FEATURE_PREFIX, ClientFeature.class);
    }

    public static void init() {
        // Start Core first.
        CharmCoreClient.init();

        if (INSTANCE == null) {
            INSTANCE = new CharmClient();
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
