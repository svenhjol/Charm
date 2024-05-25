package svenhjol.charm.feature.piglin_pointing.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import svenhjol.charm.feature.piglin_pointing.PiglinPointing;
import svenhjol.charm.foundation.feature.AdvancementHolder;
import svenhjol.charm.foundation.helper.PlayerHelper;

public final class Advancements extends AdvancementHolder<PiglinPointing> {
    public Advancements(PiglinPointing feature) {
        super(feature);
    }

    public void piglinProvidedDirections(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> trigger("piglin_provided_directions", player));
    }
}
