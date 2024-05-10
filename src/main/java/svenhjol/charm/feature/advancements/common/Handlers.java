package svenhjol.charm.feature.advancements.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.feature.FeatureHolder;
import svenhjol.charm.foundation.helper.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

public final class Handlers extends FeatureHolder<Advancements> {
    public List<String> fuzzyRemove = new ArrayList<>();
    public List<String> exactRemove = new ArrayList<>();

    public Handlers(Advancements feature) {
        super(feature);
    }

    public void packReload(String reason) {
        log().debug("Reloading Charm custom advancement filtering: " + reason);

        exactRemove.clear();
        fuzzyRemove.clear();

        for (var condition : feature().registers.conditions) {
            if (condition.test()) continue;
            condition.advancements().forEach(remove -> {
                if (remove.contains("*") || !remove.contains(":")) {
                    fuzzyRemove.add(remove);
                } else {
                    exactRemove.add(remove);
                }
            });
        }
    }

    public boolean shouldRemove(ResourceLocation id) {
        return ResourceLocationHelper.isDisabledCharmonyFeature(id)
            || ResourceLocationHelper.match(id, exactRemove, fuzzyRemove);
    }

    /**
     * Call by any mod to trigger the ActionPerformed advancement.
     */
    @SuppressWarnings("unused")
    public void trigger(ResourceLocation advancement, Player player) {
        // Don't do anything on the client.
        if (!player.level().isClientSide) {
            feature().registers.actionPerformed.trigger(advancement, (ServerPlayer)player);
        }
    }
}