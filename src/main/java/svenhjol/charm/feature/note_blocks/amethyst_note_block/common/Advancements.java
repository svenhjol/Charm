package svenhjol.charm.feature.note_blocks.amethyst_note_block.common;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import svenhjol.charm.charmony.helper.PlayerHelper;
import svenhjol.charm.feature.core.custom_advancements.common.AdvancementHolder;
import svenhjol.charm.feature.note_blocks.amethyst_note_block.AmethystNoteBlock;

public final class Advancements extends AdvancementHolder<AmethystNoteBlock> {
    public Advancements(AmethystNoteBlock feature) {
        super(feature);
    }

    public void playedAmethystNoteBlock(Level level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 4.0d).forEach(
            player -> trigger("played_amethyst_note_block", player));
    }
}
