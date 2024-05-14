package svenhjol.charm.feature.storage_blocks.ender_pearl_block.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.storage_blocks.ender_pearl_block.EnderPearlBlock;
import svenhjol.charm.foundation.feature.AdvancementHolder;
import svenhjol.charm.foundation.helper.PlayerHelper;

public final class Advancements extends AdvancementHolder<EnderPearlBlock> {
    public Advancements(EnderPearlBlock feature) {
        super(feature);
    }

    public void convertedSilverfish(Level level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 8.0d).forEach(
            player -> trigger("converted_silverfish", player));
    }
}
