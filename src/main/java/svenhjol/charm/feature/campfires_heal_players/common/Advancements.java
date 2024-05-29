package svenhjol.charm.feature.campfires_heal_players.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.campfires_heal_players.CampfiresHealPlayers;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<CampfiresHealPlayers> {
    public Advancements(CampfiresHealPlayers feature) {
        super(feature);
    }

    public void healedNearCampfire(Player player) {
        trigger("healed_near_campfire", player);
    }
}
