package svenhjol.charm.feature.pigs_find_mushrooms.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.pigs_find_mushrooms.PigsFindMushrooms;
import svenhjol.charm.foundation.feature.AdvancementHolder;
import svenhjol.charm.foundation.helper.PlayerHelper;

public final class Advancements extends AdvancementHolder<PigsFindMushrooms> {
    public Advancements(PigsFindMushrooms feature) {
        super(feature);
    }

    public void unearthedMushroom(Level level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> trigger("unearthed_mushroom", player));
    }
}
