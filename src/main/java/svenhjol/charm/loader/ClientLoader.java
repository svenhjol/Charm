package svenhjol.charm.loader;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.ClientModule;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class ClientLoader<T extends CharmModule> extends ModuleLoader<T> {
    private static final Map<ResourceLocation, CharmModule> ALL_MODULES = new HashMap<>();

    public ClientLoader(String modId, String basePackage) {
        super(modId, basePackage);
        getModules().forEach(module -> ALL_MODULES.put(module.getId(), module));
    }

    @Override
    protected String getModuleAnnotation() {
        return "Lsvenhjol/charm/annotation/ClientModule;";
    }

    @Override
    protected void setupModuleAnnotations(Class<T> clazz, T module) throws IllegalStateException {
        if (clazz.isAnnotationPresent(ClientModule.class)) {
            ClientModule annotation = clazz.getAnnotation(ClientModule.class);

            module.setModId(getModId());
            module.setPriority(annotation.priority());
            module.addDependencyCheck(m -> Charm.LOADER.isEnabled(annotation.module()));
        } else {
            throw new RuntimeException("[ClientLoader] Missing annotation for client module `" + clazz + "`");
        }
    }

    public static Map<ResourceLocation, CharmModule> getAllModules() {
        return ALL_MODULES;
    }
}
