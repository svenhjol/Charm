package svenhjol.charm.helper;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @version 1.0.1-charm
 */
public class AdvancementHelper {
    private static final List<ResourceLocation> ADVANCEMENTS_TO_REMOVE = new ArrayList<>();

    public static void removeAdvancement(ResourceLocation id) {
        LogHelper.debug(AdvancementHelper.class, "Adding `" + id + "` to list of advancements to remove");
        ADVANCEMENTS_TO_REMOVE.add(id);
    }

    public static void filterAdvancements(Map<ResourceLocation, Advancement.Builder> map) {
        if (ADVANCEMENTS_TO_REMOVE.isEmpty() || map.isEmpty()) return;

        ADVANCEMENTS_TO_REMOVE.forEach(toRemove -> {
            List<ResourceLocation> keys = new ArrayList<>(map.keySet());

            // remove exact matches
            AtomicInteger exactMatches = new AtomicInteger();
            keys.stream().filter(a -> a.equals(toRemove)).forEach(a -> {
                LogHelper.debug(AdvancementHelper.class, "> Filtering out exact match `" + a + "`");
                exactMatches.getAndIncrement();
                map.remove(a);
            });
            if (exactMatches.intValue() > 0)
                return;

            // remove all advancements for disabled modules
            keys.stream()
                .filter(a -> a.getNamespace().equals(toRemove.getNamespace()))
                .filter(a -> a.getPath().startsWith(toRemove.getPath()))
                .forEach(a -> {
                    LogHelper.debug(AdvancementHelper.class, "> Filtering out fuzzy match `" + a + "`");
                    map.remove(a);
                });
        });
    }

    public static <T extends CharmModule> void removeAdvancements(List<T> modules) {
        modules.forEach(module -> removeAdvancement(module.getId()));
    }
}
