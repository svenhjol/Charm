package svenhjol.charm.loader;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.annotation.ClientModule;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;

@SuppressWarnings({"unused", "unchecked"})
public class ClientLoader<T extends CharmModule, L extends ModuleLoader<T>> extends ModuleLoader<T> {
    private static final Map<ResourceLocation, CharmModule> ALL_MODULES_BY_ID = new HashMap<>();
    private final Map<Class<? extends T>, T> MODULES_BY_CLASS = new HashMap<>();

    private final L loader;

    public ClientLoader(String modId, L loader, String basePackage) {
        super(modId, basePackage);
        this.loader = loader;
        getModules().forEach(module -> {
            ALL_MODULES_BY_ID.put(module.getId(), module);
            MODULES_BY_CLASS.put((Class<? extends T>) module.getClass(), module);
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

    @Nullable
    public T getModule(Class<? extends T> clazz) {
        return MODULES_BY_CLASS.getOrDefault(clazz, null);
    }

    public static Map<ResourceLocation, CharmModule> getAllModules() {
        return ALL_MODULES_BY_ID;
    }
}
