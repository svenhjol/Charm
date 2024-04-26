package svenhjol.charm.feature.amethyst_note_block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import svenhjol.charm.Charm;
import svenhjol.charm.feature.advancements.Advancements;
import svenhjol.charm.foundation.helper.PlayerHelper;

public class CommonCallbacks {
    public static void triggerPlayedAmethystNoteBlock(Level level, BlockPos pos) {
        PlayerHelper.getPlayersInRange(level, pos, 4.0d).forEach(
            player -> Advancements.trigger(Charm.id("played_amethyst_note_block"), player)
        );
    }
}
