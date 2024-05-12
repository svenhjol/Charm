package svenhjol.charm.feature.campfires_boost_health.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.campfires_boost_health.CampfiresBoostHealth;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<CampfiresBoostHealth> {
    public Advancements(CampfiresBoostHealth feature) {
        super(feature);
    }

    public void healedNearCampfire(Player player) {
        trigger("healed_near_campfire", player);
    }
}
