package svenhjol.charm.feature.core.custom_advancements.common;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.core.custom_advancements.CustomAdvancements;
import svenhjol.charm.charmony.common.helper.CommonFeatureHelper;
import svenhjol.charm.charmony.feature.FeatureHolder;
import svenhjol.charm.charmony.helper.ResourceLocationHelper;

import java.util.ArrayList;
import java.util.List;

public final class Handlers extends FeatureHolder<CustomAdvancements> {
    private final List<String> fuzzyRemove = new ArrayList<>();
    private final List<String> exactRemove = new ArrayList<>();

    public Handlers(CustomAdvancements feature) {
        super(feature);
    }

    public void packReload(String reason) {
        feature().log().debug("Reloading Charm custom advancement filtering: " + reason);

        exactRemove.clear();
        fuzzyRemove.clear();

        for (var condition : feature().providers.conditions) {
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
        return CommonFeatureHelper.isDisabledCharmonyFeature(id)
            || ResourceLocationHelper.match(id, exactRemove, fuzzyRemove);
    }

    /**
     * Call by any mod to trigger the ActionPerformed advancement.
     */
    public void trigger(ResourceLocation advancement, Player player) {
        // Don't do anything on the client.
        if (!player.level().isClientSide) {
            feature().registers.actionPerformed.trigger(advancement, (ServerPlayer)player);
        }
    }
}
