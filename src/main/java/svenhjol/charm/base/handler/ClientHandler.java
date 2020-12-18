package svenhjol.charm.base.handler;

import com.google.common.collect.ImmutableList;
import svenhjol.charm.CharmClient;
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
    public static Map<String, CharmClientModule> LOADED_MODULES = new TreeMap<>();
    private static boolean hasInit = false;
    private static List<Class<? extends CharmClientModule>> ENABLED_MODULES = new ArrayList<>(); // this is a cache of enabled classes

    public static ClientHandler INSTANCE = new ClientHandler();

    private ClientHandler() {}

    public void registerFabricMod(String modId) {
        List<Class<? extends CharmModule>> available = ModuleHandler.AVAILABLE_MODULES.getOrDefault(modId, ImmutableList.of());

        available.forEach(moduleClass -> {
            String name = moduleClass.getSimpleName();

            if (ModuleHandler.LOADED_MODULES.containsKey(name)) {
                CharmModule module = ModuleHandler.LOADED_MODULES.get(name);
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
                CharmClient.LOG.info("Loaded client module " + moduleName);
                client.register();
            }
        });
    }

    public void init() {
        if (hasInit)
            return;

        // post init, only enabled modules are run
        eachEnabledModule(clientModule -> {
            // this is a cache for quick lookup of enabled classes
            ENABLED_MODULES.add(clientModule.getClass());
            clientModule.init();
        });

        ClientJoinCallback.EVENT.register(client -> {
            ColoredGlintHandler.init();
            DecorationHandler.init();
            eachEnabledModule(m -> m.loadWorld(client));
        });

        hasInit = true;
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

    private static void eachModule(Consumer<CharmClientModule> consumer) {
        LOADED_MODULES.values().forEach(consumer);
    }

    private static void eachEnabledModule(Consumer<CharmClientModule> consumer) {
        LOADED_MODULES.values()
            .stream()
            .filter(m -> m.getModule().enabled)
            .forEach(consumer);
    }
}
