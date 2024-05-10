package svenhjol.charm.foundation.advancement.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.api.iface.IConditionalAdvancement;
import svenhjol.charm.foundation.advancement.AdvancementManager;
import svenhjol.charm.foundation.helper.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

public final class Handlers {
    private static final List<String> FUZZY_REMOVE = new ArrayList<>();
    private static final List<String> EXACT_REMOVE = new ArrayList<>();
    public static final List<IConditionalAdvancement> CONDITIONS = new ArrayList<>();

    public static void packReload(String reason) {
        AdvancementManager.LOGGER.debug("Reloading Charm custom advancement filtering: " + reason);

        EXACT_REMOVE.clear();
        FUZZY_REMOVE.clear();

        for (var condition : CONDITIONS) {
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

    /**
     * Call by any mod to trigger the ActionPerformed advancement.
     */
    public static void trigger(ResourceLocation advancement, Player player) {
        // Don't do anything on the client.
        if (!player.level().isClientSide) {
            AdvancementManager.instance().actionPerformed.trigger(advancement, (ServerPlayer)player);
        }
    }
}
