package svenhjol.charm.feature.suspicious_block_creating.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import svenhjol.charm.feature.suspicious_block_creating.SuspiciousBlockCreating;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;
import svenhjol.charm.charmony.helper.PlayerHelper;

public final class Advancements extends AdvancementHolder<SuspiciousBlockCreating> {
    public Advancements(SuspiciousBlockCreating feature) {
        super(feature);
    }

    public void createdSuspiciousBlock(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> trigger("created_suspicious_block", player));
    }
}
