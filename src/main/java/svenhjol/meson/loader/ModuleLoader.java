package svenhjol.meson.loader;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import svenhjol.meson.MesonModule;
import svenhjol.meson.Meson;
import svenhjol.meson.MesonLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static svenhjol.meson.MesonLoader.LOAD_MODULE;

public class ModuleLoader
{
    // should match the properties of the annotation
    public static final String MOD = "mod";
    public static final String CATEGORY = "category";
    public static final String NAME = "name";
    public static final String DESCRIPTION = "description";
    public static final String HAS_SUBSCRIPTIONS = "hasSubscriptions";
    public static final String ENABLED_BY_DEFAULT = "enabledByDefault";
    public static final String CONFIGURE_ENABLED = "configureEnabled";
    public static final String CLIENT = "client";
    public static final String SERVER = "server";

    public ModuleLoader(MesonLoader instance)
    {
        // instantiate and gather all modules into categories
        ModFileScanData result = ModList.get().getModFileById(instance.id).getFile().getScanResult();
        List<AnnotationData> targets = result.getAnnotations().stream()
            .filter(annotationData -> LOAD_MODULE.equals(annotationData.getAnnotationType()))
            .collect(Collectors.toList());

        targets.forEach(target -> {
            try {
                String moduleClass = target.getClassType().getClassName();
                Map<String, Object> data = target.getAnnotationData();

                if (data.containsKey(CLIENT) && !(boolean)data.get(CLIENT) && FMLEnvironment.dist == Dist.CLIENT) return;
                if (data.containsKey(SERVER) && !(boolean)data.get(SERVER) && FMLEnvironment.dist == Dist.DEDICATED_SERVER) return;

                MesonModule module = (MesonModule) Class.forName(moduleClass).newInstance();

                String mod = (String) data.get(MOD);
                module.mod = mod;

                String category = (String) data.get(CATEGORY);
                module.category = category;

                if (data.containsKey(NAME)) {
                    module.name = (String) data.get(NAME);
                } else {
                    module.name = module.getClass().getSimpleName();
                }

                if (data.containsKey(DESCRIPTION)) {
                    module.description = (String) data.get(DESCRIPTION);
                }

                if (data.containsKey(HAS_SUBSCRIPTIONS)) {
                    module.hasSubscriptions = (Boolean) data.get(HAS_SUBSCRIPTIONS);
                }

                if (data.containsKey(ENABLED_BY_DEFAULT)) {
                    module.enabledByDefault = (Boolean) data.get(ENABLED_BY_DEFAULT);
                }

                if (data.containsKey(CONFIGURE_ENABLED)) {
                    module.configureEnabled = (Boolean) data.get(CONFIGURE_ENABLED);
                }

                if (!instance.categories.containsKey(category)) {
                    instance.categories.put(category, new ArrayList<>());
                }

                // add to category and to full module set
                instance.categories.get(category).add(module);
                instance.modules.add(module);

            } catch (ReflectiveOperationException e) {
                Meson.warn("Failed to load module " + target.toString());
            }
        });
    }
}
