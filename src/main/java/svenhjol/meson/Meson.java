package svenhjol.meson;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import svenhjol.meson.event.*;
import svenhjol.meson.handler.BiomeHandler;
import svenhjol.meson.handler.DecorationHandler;
import svenhjol.meson.handler.LogHandler;
import svenhjol.meson.helper.StringHelper;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Meson {
    public static Meson INSTANCE = new Meson();
    public static LogHandler LOG = new LogHandler("Meson");
    public static Map<String, Map<String, MesonModule>> loadedModules = new ConcurrentHashMap<>();
    private static Map<String, MesonMod> mods = new ConcurrentHashMap<>();

    private Meson() {
    }

    public void register(MesonMod mod) {
        mods.put(mod.getId(), mod);
    }

    public static void go() {
        BiomeHandler.init();

        // early init, use for registering blocks etc.
        mods.forEach((id, mod) -> {
            mod.eachModule(MesonModule::register);
            if (Meson.isClient())
                mod.eachModule(MesonModule::clientRegister);
        });

        // post init, only enabled modules are run
        mods.forEach((id, mod) -> {
            mod.eachEnabledModule(MesonModule::init);
            if (Meson.isClient())
                mod.eachEnabledModule(MesonModule::clientInit);
        });

        // allow mods to modify structures in a controlled way
        StructureSetupCallback.EVENT.invoker().interact();

        // listen for world loading events
        LoadWorldCallback.EVENT.register(server -> {
            initServerHandlers();
            mods.forEach((id, mod) -> mod.eachEnabledModule(m -> m.loadWorld(server)));
        });

        // listen for client join events (client only)
        if (isClient()) {
            ClientReloadPacksCallback.EVENT.register(client -> {
                mods.forEach((id, mod) -> mod.eachEnabledModule(m -> m.clientReloadPacks(client)));
            });

            ClientJoinCallback.EVENT.register(client -> {
                initClientHandlers();
                mods.forEach((id, mod) -> mod.eachEnabledModule(m -> m.clientJoinWorld(client)));
            });
        }

        // listen for server setup events (dedicated server only)
        DedicatedServerSetupCallback.EVENT.register(server -> {
            initServerHandlers();
            mods.forEach((id, mod) -> mod.eachEnabledModule(m -> m.dedicatedServerInit(server)));
        });
    }

    public static MesonMod getMod(String id) {
        if (!mods.containsKey(id))
            throw new RuntimeException("No such mod: " + id);

        return mods.get(id);
    }

    @Nullable
    public static MesonModule getModule(String moduleName) {
        String[] split = moduleName.split(":");
        String mod = split[0];
        String module = split[1];

        if (!loadedModules.containsKey(mod))
            return null;

        module = StringHelper.snakeToUpperCamel(module);

        if (!loadedModules.get(mod).containsKey(module))
            return null;

        return loadedModules.get(mod).get(module);
    }

    public static boolean enabled(String moduleName) {
        MesonModule module = getModule(moduleName);
        return module != null && module.enabled;
    }

    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }

    private static void initClientHandlers() {
        DecorationHandler.init(); // load late so that tags are populated at this point
    }

    private static void initServerHandlers() {
        DecorationHandler.init(); // load late so that tags are populated at this point
    }
}
