package svenhjol.meson.compat;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import svenhjol.meson.Meson;
import vazkii.quark.api.event.ModuleLoadedEvent;
import vazkii.quark.api.event.ModuleStateChangedEvent;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleLoader;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "unchecked"})
public class QuarkCompat {
    public Map<Class<? extends Module>, Module> foundModules;
    public Map<String, Boolean> modules = new HashMap<>();
    public Map<String, Module> nameMap = new HashMap<>();

    public QuarkCompat() {
        // TODO this is not a great way to get Quark modules but events aren't firing
        Object found = ObfuscationReflectionHelper.getPrivateValue(ModuleLoader.class, ModuleLoader.INSTANCE, "foundModules");
        if (found == null) {
            Meson.LOG.error("Could not get foundModules from Quark ModuleLoader - Quark module checking will NOT be available");
            return;
        }

        foundModules = (Map<Class<? extends Module>, Module>) found;
        for (Module mod : foundModules.values()) {
            nameMap.put(mod.lowercaseName, mod);
        }
    }

    // TODO doesn't get fired when event fires
    public void onModuleLoaded(final ModuleLoadedEvent event) {
        modules.put(event.eventName, false);
    }

    // TODO doesn't get fired when event fires
    public void onModuleStateChanged(final ModuleStateChangedEvent event) {
        modules.put(event.eventName, event.enabled);
    }

    public boolean isModuleEnabled(String name) {
        return nameMap.containsKey(name) && nameMap.get(name).enabled;
        // return modules.containsKey(name) && modules.get(name); // TODO events not firing
    }

    public boolean isModulePresent(String name) {
        return nameMap.containsKey(name);
        // return modules.containsKey(name); // TODO events not firing
    }
}
