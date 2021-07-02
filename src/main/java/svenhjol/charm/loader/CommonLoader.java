package svenhjol.charm.loader;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.helper.ConfigHelper;
import svenhjol.charm.mixin.CharmMixinConfigPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommonLoader<T extends CharmModule> extends ModuleLoader<T> {
    private static final Map<ResourceLocation, CharmModule> ALL_MODULES = new HashMap<>();

    public CommonLoader(String modId, String basePackage) {
        super(modId, basePackage);
    }

    @Override
    public void register() {
        super.register();

        // after register step, add all modules to the static
        getModules().forEach(module -> ALL_MODULES.put(module.getId(), module));
    }

    @Override
    protected void dependencies() {
        // run a mixin check as part of the module dependency checks
        getModules().forEach(module -> module.addDependencyCheck(m
            -> !CharmMixinConfigPlugin.isMixinDisabled(m.getName())));

        super.dependencies();
    }

    @Override
    protected String getModuleAnnotation() {
        return "Lsvenhjol/charm/annotation/CommonModule;";
    }

    @Override
    protected void setupModuleAnnotations(Class<T> clazz, T module) throws IllegalStateException {
        if (clazz.isAnnotationPresent(CommonModule.class)) {
            CommonModule annotation = clazz.getAnnotation(CommonModule.class);
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
    protected void setupModuleConfig(List<T> modules) {
        ConfigHelper.applyConfig(getModId(), modules);
        modules.forEach(module -> module.setEnabled(!ConfigHelper.isModuleDisabled(getModId(), module.getName())));
        ConfigHelper.writeConfig(getModId(), modules);
    }

    public static Map<ResourceLocation, CharmModule> getAllModules() {
        return ALL_MODULES;
    }
}
