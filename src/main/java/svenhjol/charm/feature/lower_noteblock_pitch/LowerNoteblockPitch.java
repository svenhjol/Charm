package svenhjol.charm.feature.lower_noteblock_pitch;

import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.mixin.accessor.NoteBlockAccessor;
import svenhjol.charmony_api.event.BlockUseEvent;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;

@Feature(mod = Charm.MOD_ID, description = "Use a noteblock while sneaking to lower its pitch by one semitone.")
public class LowerNoteblockPitch extends CharmFeature {
    @Override
    public void runWhenEnabled() {
        BlockUseEvent.INSTANCE.handle(this::handleUseBlock);
    }

    private InteractionResult handleUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND
            || level.isClientSide
            || !player.getMainHandItem().isEmpty()
            || !player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }

        var pos = hitResult.getBlockPos();
        var state = level.getBlockState(pos);
        var block = state.getBlock();

        if (block != Blocks.NOTE_BLOCK) {
            return InteractionResult.PASS;
        }

        int currentNote = state.getValue(NoteBlock.NOTE);
        if (currentNote == 0) {
            state = state.setValue(NoteBlock.NOTE, NoteBlock.NOTE.getPossibleValues().size() - 1);
        } else {
            state = state.setValue(NoteBlock.NOTE, currentNote - 1);
        }

        level.setBlock(pos, state, 3);
        ((NoteBlockAccessor)block).invokePlayNote(null, state, level, pos);
        player.awardStat(Stats.TUNE_NOTEBLOCK);
        return InteractionResult.CONSUME;
    }
}
