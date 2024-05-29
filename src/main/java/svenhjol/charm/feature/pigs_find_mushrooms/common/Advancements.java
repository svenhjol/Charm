package svenhjol.charm.feature.pigs_find_mushrooms.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import svenhjol.charm.charmony.helper.PlayerHelper;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;
import svenhjol.charm.feature.pigs_find_mushrooms.PigsFindMushrooms;

public final class Advancements extends AdvancementHolder<PigsFindMushrooms> {
    public Advancements(PigsFindMushrooms feature) {
        super(feature);
    }

    public void unearthedMushroom(Level level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> trigger("unearthed_mushroom", player));
    }
}
