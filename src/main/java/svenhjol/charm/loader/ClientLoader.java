package svenhjol.charm.loader;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.annotation.ClientModule;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@SuppressWarnings({"unused", "unchecked"})
public class ClientLoader<T extends CharmModule, L extends ModuleLoader<T>> extends ModuleLoader<T> {
    private final Map<ResourceLocation, T> ALL_MODULES_BY_ID = new HashMap<>();
    private final Map<Class<? extends T>, T> ALL_MODULES_BY_CLASS = new HashMap<>();

    private final L loader;

    public ClientLoader(String modId, L loader, String basePackage) {
        super(modId, basePackage);
        this.loader = loader;
        getModules().forEach(module -> {
            ALL_MODULES_BY_ID.put(module.getId(), module);
            ALL_MODULES_BY_CLASS.put((Class<? extends T>) module.getClass(), module);
        });
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
            module.addDependencyCheck(m -> loader.isEnabled((Class<? extends T>) annotation.module()));
        } else {
            throw new RuntimeException("[ClientLoader] Missing annotation for client module `" + clazz + "`");
        }
    }

    public Optional<T> getModule(Class<? extends T> clazz) {
        return Optional.ofNullable(ALL_MODULES_BY_CLASS.get(clazz));
    }

    public Map<ResourceLocation, T> getAllModules() {
        return ALL_MODULES_BY_ID;
    }
}
