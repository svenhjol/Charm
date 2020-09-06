package svenhjol.meson;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import svenhjol.meson.event.CommonSetupCallback;
import svenhjol.meson.event.StructureSetupCallback;
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

        // allow mods to modify structures in a controlled way
        StructureSetupCallback.EVENT.invoker().interact();

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
}
