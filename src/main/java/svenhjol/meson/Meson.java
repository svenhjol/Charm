package svenhjol.meson;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import svenhjol.charm.event.CommonSetupCallback;
import svenhjol.meson.handler.LogHandler;
import svenhjol.meson.helper.StringHelper;

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
        // early init, use for registering blocks etc.
        mods.forEach((id, mod) -> {
            mod.eachModule(MesonModule::init);
            if (Meson.isClient())
                mod.eachModule(MesonModule::initClient);
        });

        // post init, only enabled modules are run
        mods.forEach((id, mod) -> {
            mod.eachEnabledModule(MesonModule::afterInit);
            if (Meson.isClient())
                mod.eachEnabledModule(MesonModule::afterInitClient);
        });

        // listen for common setup events
        CommonSetupCallback.EVENT.register(() -> {
            mods.forEach((id, mod) -> {
                mod.eachEnabledModule(MesonModule::setup);

                if (Meson.isClient())
                    mod.eachEnabledModule(MesonModule::setupClient);
            });
        });
    }

    public static MesonMod getMod(String id) {
        if (!mods.containsKey(id))
            throw new RuntimeException("No such mod: " + id);

        return mods.get(id);
    }

    public static boolean enabled(String moduleName) {
        String[] split = moduleName.split(":");
        String mod = split[0];
        String module = split[1];

        if (!loadedModules.containsKey(mod))
            return false;

        module = StringHelper.snakeToUpperCamel(module);

        if (!loadedModules.get(mod).containsKey(module))
            return false;

        return loadedModules.get(mod).get(module).enabled;
    }

    public static boolean isClient() {
        return FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT;
    }
}
