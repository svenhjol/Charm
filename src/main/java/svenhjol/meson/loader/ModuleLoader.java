package svenhjol.meson.loader;

import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import net.minecraftforge.forgespi.language.ModFileScanData.AnnotationData;
import svenhjol.meson.Feature;
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
                String featureClass = target.getClassType().getClassName();
                Feature feature = (Feature) Class.forName(featureClass).newInstance();
                Map<String, Object> data = target.getAnnotationData();

                String category = (String) data.get("category");
                feature.category = category;

                if (data.containsKey("name")) {
                    feature.name = (String) data.get("name");
                }

                if (data.containsKey("hasSubscriptions")) {
                    feature.hasSubscriptions = (Boolean) data.get("hasSubscriptions");
                }

                if (data.containsKey("enabledByDefault")) {
                    feature.enabledByDefault = (Boolean) data.get("enabledByDefault");
                }

                if (!instance.categories.containsKey(category)) {
                    instance.categories.put(category, new ArrayList<>());
                }
                instance.categories.get(category).add(feature);

            } catch (ReflectiveOperationException e) {
                Meson.warn("Failed to load module " + target.toString());
            }
        });
    }
}
