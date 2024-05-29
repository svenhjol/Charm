package svenhjol.charm.feature.storage_blocks.gunpowder_block.common;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import svenhjol.charm.feature.storage_blocks.gunpowder_block.GunpowderBlock;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;
import svenhjol.charm.charmony.helper.PlayerHelper;

public final class Advancements extends AdvancementHolder<GunpowderBlock> {
    public Advancements(GunpowderBlock feature) {
        super(feature);
    }

    public void dissolvedGunpowder(ServerLevel level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(player
            -> trigger("dissolved_gunpowder", player));
    }
}
