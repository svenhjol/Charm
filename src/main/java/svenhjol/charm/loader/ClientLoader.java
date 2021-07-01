package svenhjol.charm.loader;

import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

public class ClientLoader<T extends ClientModule, U extends CommonModule> extends ModuleLoader<T> {
    private static final Map<ResourceLocation, ClientModule> ALL_MODULES = new HashMap<>();

    private final CommonLoader<U> loader;

    public ClientLoader(CommonLoader<U> loader, String modId, String basePackage) {
        super(modId, basePackage);
        this.loader = loader;

        getModules().forEach(module -> ALL_MODULES.put(module.getId(), module));
    }

    @Override
    protected String getModuleAnnotation() {
        return "Lsvenhjol/charm/annotation/ClientModule;";
    }

    @Override
    protected void setupModuleAnnotations(Class<T> clazz, T module) throws IllegalStateException {
        if (clazz.isAnnotationPresent(svenhjol.charm.annotation.ClientModule.class)) {
            svenhjol.charm.annotation.ClientModule annotation = clazz.getAnnotation(svenhjol.charm.annotation.ClientModule.class);

            Class<? extends CommonModule> parentModuleClazz = annotation.module();
            String name = parentModuleClazz.getSimpleName();
            U parentModule = loader.getModule(name);
            if (parentModule == null)
                throw new IllegalStateException("Client requested a module that does not exist: " + name);

            module.setModId(getModId());
            module.setParentModule(parentModule);
            module.setPriority(annotation.priority());
        } else {
            throw new RuntimeException("Missing annotation for client module: " + clazz);
        }
    }

    public static Map<ResourceLocation, ClientModule> getAllModules() {
        return ALL_MODULES;
    }
}
