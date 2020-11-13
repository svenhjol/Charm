package svenhjol.charm.base.handler;

import svenhjol.charm.CharmClient;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.StringHelper;
import svenhjol.charm.event.ClientJoinCallback;
import svenhjol.charm.handler.ColoredGlintHandler;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Consumer;

public class ClientHandler {
    public static Map<String, CharmClientModule> LOADED_MODULES = new TreeMap<>();
    private static boolean hasInit = false;

    public static void init() {
        if (hasInit)
            return;

        // create all charm-based client modules
        instantiateModules();

        // early init, always run, use for registering things
        eachModule(CharmClientModule::register);

        // post init, only enabled modules are run
        eachEnabledModule(CharmClientModule::init);

        ClientJoinCallback.EVENT.register(client -> {
            ColoredGlintHandler.init();
            DecorationHandler.init();
            eachEnabledModule(m -> m.loadWorld(client));
        });

        hasInit = true;
    }

    private static void instantiateModules() {
        ModuleHandler.LOADED_MODULES.forEach((modId, module) -> {
            CharmClientModule client;

            Class<? extends CharmClientModule> clazz = module.client;
            if (clazz == null || clazz == CharmClientModule.class)
                return;

            try {
                 client = clazz.getConstructor(CharmModule.class).newInstance(module);
            } catch (Exception e) {
                CharmClient.LOG.error("Failed to create the client for " + module.getName());
                throw new RuntimeException("The chickens escaped");
            }

            String moduleName = module.getName();
            ClientHandler.LOADED_MODULES.put(moduleName, client);
        });
    }

    @Nullable
    public static CharmClientModule getModule(String moduleName) {
        return LOADED_MODULES.getOrDefault(StringHelper.snakeToUpperCamel(moduleName), null);
    }

    private static void eachModule(Consumer<CharmClientModule> consumer) {
        LOADED_MODULES.values().forEach(consumer);
    }

    private static void eachEnabledModule(Consumer<CharmClientModule> consumer) {
        LOADED_MODULES.values()
            .stream()
            .filter(m -> m.enabled)
            .forEach(consumer);
    }
}
