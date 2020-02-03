package svenhjol.meson.compat;

import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import vazkii.quark.base.module.Module;
import vazkii.quark.base.module.ModuleLoader;

import java.util.HashMap;
import java.util.Map;

public class QuarkModules
{
    public Map<Class<? extends Module>, Module> foundModules;
    public Map<String, Module> nameMap = new HashMap<>();

    public QuarkModules() throws Exception
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

    public boolean hasModule(String name)
    {
        return nameMap.containsKey(name) && nameMap.get(name).enabled;
    }
}
