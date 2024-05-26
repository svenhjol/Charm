package svenhjol.charm.feature.raid_horns.common;

import net.minecraft.world.entity.player.Player;
import svenhjol.charm.feature.raid_horns.RaidHorns;
import svenhjol.charm.foundation.feature.AdvancementHolder;

public final class Advancements extends AdvancementHolder<RaidHorns> {
    public Advancements(RaidHorns feature) {
        super(feature);
    }

    public void calledOffRaid(Player player) {
        trigger("called_off_raid", player);
    }
}
