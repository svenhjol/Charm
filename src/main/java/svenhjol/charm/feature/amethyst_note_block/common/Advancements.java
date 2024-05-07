package svenhjol.charm.feature.amethyst_note_block.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import svenhjol.charm.feature.amethyst_note_block.AmethystNoteBlock;
import svenhjol.charm.foundation.feature.Advancement;
import svenhjol.charm.foundation.helper.PlayerHelper;

public final class Advancements extends Advancement<AmethystNoteBlock> {
    public void playedNoteBlock(Level level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 4.0d).forEach(
            player -> trigger("played_amethyst_note_block", player)
        );
    }

    @Override
    protected Class<AmethystNoteBlock> type() {
        return AmethystNoteBlock.class;
    }
}
