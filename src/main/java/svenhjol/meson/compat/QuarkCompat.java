package svenhjol.meson.compat;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.quark.api.event.ModuleLoadedEvent;
import vazkii.quark.api.event.ModuleStateChangedEvent;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleLoader;

import java.util.HashMap;
import java.util.Map;

public class QuarkCompat
{
    public Map<Class<? extends Module>, Module> foundModules;
    public Map<String, Boolean> modules = new HashMap<>();
    public Map<String, Module> nameMap = new HashMap<>();

    public QuarkCompat() throws Exception
    {
        Object found = ObfuscationReflectionHelper.getPrivateValue(ModuleLoader.class, ModuleLoader.INSTANCE, "foundModules");
        if (found == null) {
            throw new Exception("Could not get foundModules from Quark ModuleLoader");
        }
        foundModules = (Map<Class<? extends Module>, Module>)found;
        for (Module mod : foundModules.values()) {
            nameMap.put(mod.lowercaseName, mod);
        }
    }

    public void onModuleLoaded(final ModuleLoadedEvent event)
    {
        modules.put(event.eventName, false);
    }

    public void onModuleStateChanged(final ModuleStateChangedEvent event)
    {
        modules.put(event.eventName, event.enabled);
    }

    public boolean isModuleEnabled(String name)
    {
        return nameMap.containsKey(name) && nameMap.get(name).enabled;
    }
}
