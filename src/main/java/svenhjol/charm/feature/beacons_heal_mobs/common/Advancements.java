package svenhjol.charm.feature.beacons_heal_mobs.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import svenhjol.charm.charmony.helper.PlayerHelper;
import svenhjol.charm.feature.beacons_heal_mobs.BeaconsHealMobs;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;

public final class Advancements extends AdvancementHolder<BeaconsHealMobs> {
    public Advancements(BeaconsHealMobs feature) {
        super(feature);
    }

    public void healedNearBeacon(Level level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> trigger("healed_near_beacon", player));
    }
}
