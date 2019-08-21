package svenhjol.meson.loader;

import net.minecraftforge.fml.ModList;
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
                MesonModule module = (MesonModule) Class.forName(moduleClass).newInstance();
                Map<String, Object> data = target.getAnnotationData();

                String category = (String) data.get("category");
                module.category = category;

                if (data.containsKey("name")) {
                    module.name = (String) data.get("name");
                }

                if (data.containsKey("description")) {
                    module.description = (String) data.get("description");
                }

                if (data.containsKey("hasSubscriptions")) {
                    module.hasSubscriptions = (Boolean) data.get("hasSubscriptions");
                }

                if (data.containsKey("enabledByDefault")) {
                    module.enabledByDefault = (Boolean) data.get("enabledByDefault");
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
