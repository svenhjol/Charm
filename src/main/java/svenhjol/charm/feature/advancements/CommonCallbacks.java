package svenhjol.charm.feature.advancements;

import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.foundation.helper.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

public final class CommonCallbacks {
    static final List<String> FUZZY_REMOVE = new ArrayList<>();
    static final List<String> EXACT_REMOVE = new ArrayList<>();

    public static void handlePackReload(String reason) {
        Advancements.LOGGER.debug("Reloading Charm custom advancement filtering: " + reason);

        EXACT_REMOVE.clear();
        FUZZY_REMOVE.clear();

        for (var condition : Advancements.CONDITIONS) {
            if (condition.test()) continue;
            condition.advancements().forEach(remove -> {
                if (remove.contains("*") || !remove.contains(":")) {
                    FUZZY_REMOVE.add(remove);
                } else {
                    EXACT_REMOVE.add(remove);
                }
            });
        }
    }

    public static boolean shouldRemove(ResourceLocation id) {
        return ResourceLocationHelper.isDisabledCharmonyFeature(id)
            || ResourceLocationHelper.match(id, EXACT_REMOVE, FUZZY_REMOVE);
    }
}
