package svenhjol.charm.base.handler;

import com.google.common.collect.ImmutableList;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charm.base.CharmClientLoader;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.StringHelper;
import svenhjol.charm.event.ClientJoinCallback;
import svenhjol.charm.handler.ColoredGlintHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class ClientHandler {
    public static ClientHandler INSTANCE = new ClientHandler();
    public static Map<String, CharmClientModule> LOADED_MODULES = new TreeMap<>();

    private final static List<Class<? extends CharmClientModule>> ENABLED_MODULES = new ArrayList<>(); // this is a cache of enabled classes

    private ClientHandler() {
        ClientJoinCallback.EVENT.register(client -> {
            ColoredGlintHandler.init();
            DecorationHandler.init();
        });
    }

    public void register(CharmClientModule module) {
        LOADED_MODULES.put(module.getName(), module);

        CharmClient.LOG.debug("Registering module " + module.getName());
        module.register();
    }

    public void init(CharmClientModule module) {
        // this is a cache for quick lookup of enabled classes
        ENABLED_MODULES.add(module.getClass());

        CharmClient.LOG.info("Initialising module " + module.getName());
        module.init();
    }

    @Nullable
    public static CharmClientModule getModule(String moduleName) {
        return LOADED_MODULES.getOrDefault(StringHelper.snakeToUpperCamel(moduleName), null);
    }

    /**
     * Use this within static hook methods for quick check if a module is enabled.
     * @param clazz Module to check
     * @return True if the module is enabled
     */
    public static boolean enabled(Class<? extends CharmClientModule> clazz) {
        return ENABLED_MODULES.contains(clazz);
    }
}
