package svenhjol.charm.feature.storage_blocks.sugar_block.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import svenhjol.charm.feature.storage_blocks.sugar_block.SugarBlock;
import svenhjol.charm.foundation.feature.AdvancementHolder;
import svenhjol.charm.foundation.helper.PlayerHelper;

public final class Advancements extends AdvancementHolder<SugarBlock> {
    public Advancements(SugarBlock feature) {
        super(feature);
    }

    public void dissolvedSugar(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> trigger("dissolved_sugar", player));
    }
}
