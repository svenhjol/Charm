package svenhjol.charm.loader;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.helper.ConfigHelper;

import java.util.HashMap;
import java.util.Map;

public class CommonLoader<T extends CommonModule> extends ModuleLoader<T> {
    private static final Map<ResourceLocation, CommonModule> ALL_MODULES = new HashMap<>();

    public CommonLoader(String modId, String basePackage) {
        super(modId, basePackage);
    }

    @Override
    public void run() {
        super.run();

        getModules().forEach(module -> ALL_MODULES.put(module.getId(), module));
    }

    @Override
    protected String getModuleAnnotation() {
        return "Lsvenhjol/charm/annotation/Module;";
    }

    @Override
    protected void setupModuleAnnotations(Class<T> clazz, T module) throws IllegalStateException {
        if (clazz.isAnnotationPresent(Module.class)) {
            Module annotation = clazz.getAnnotation(Module.class);
            module.setModId(getModId());
            module.setPriority(annotation.priority());
            module.setAlwaysEnabled(annotation.alwaysEnabled());
            module.setDescription(annotation.description());
            module.setEnabledByDefault(annotation.enabledByDefault());
            module.setEnabled(module.isEnabledByDefault());

        } else {
            throw new RuntimeException("Missing annotation for module: " + clazz);
        }
    }

    @Override
    protected void setupModuleConfig(Map<String, T> loadedModules) {
        // config for this module set
        ConfigHelper.createConfig(getModId(), loadedModules);
    }

    public static Map<ResourceLocation, CommonModule> getAllModules() {
        return ALL_MODULES;
    }
}
