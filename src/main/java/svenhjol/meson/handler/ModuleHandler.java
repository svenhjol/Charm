package svenhjol.meson.handler;

import svenhjol.meson.Meson;
import svenhjol.meson.MesonMod;
import svenhjol.meson.MesonModule;
import svenhjol.meson.iface.Module;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleHandler {
    public ModuleHandler(MesonMod mod, List<Class<? extends MesonModule>> classes) {
        Map<String, MesonModule> moduleInstances = new HashMap<>();

        classes.forEach(clazz -> {
            try {
                MesonModule module = clazz.getDeclaredConstructor().newInstance();
                if (clazz.isAnnotationPresent(Module.class)) {
                    Module annotation = clazz.getAnnotation(Module.class);
                    module.mod = mod;
                    module.alwaysEnabled = annotation.alwaysEnabled();
                    module.enabledByDefault = annotation.enabledByDefault();
                    module.enabled = module.enabledByDefault;

                    String moduleName = module.getName();
                    moduleInstances.put(moduleName, module);

                } else {
                    throw new RuntimeException("No module annotation for class " + clazz.toString());
                }

            } catch (Exception e) {
                throw new RuntimeException("Could not initialize module class: " + clazz.toString(), e);
            }
        });

        if (!Meson.loadedModules.containsKey(mod.getId()))
            Meson.loadedModules.put(mod.getId(), new HashMap<>());

        moduleInstances.forEach((moduleName, module) -> {
            String name = module.getName();
            Meson.loadedModules.get(mod.getId()).put(name, module);
        });
    }
}
