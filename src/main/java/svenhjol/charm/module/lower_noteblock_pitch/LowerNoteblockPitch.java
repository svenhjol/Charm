package svenhjol.charm.module.lower_noteblock_pitch;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.core.BlockPos;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, description = "Use a noteblock while sneaking to lower its pitch by one semitone.")
public class LowerNoteblockPitch extends CharmModule {
    @Override
    public void runWhenEnabled() {
        UseBlockCallback.EVENT.register(this::handleUseBlock);
    }

    private InteractionResult handleUseBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND
            || level.isClientSide
            || !player.getMainHandItem().isEmpty()
            || !player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }

        BlockPos pos = hitResult.getBlockPos();
        BlockState state = level.getBlockState(pos);
        Block block = state.getBlock();

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
        ((NoteBlock)block).playNote(level, pos);
        player.awardStat(Stats.TUNE_NOTEBLOCK);
        return InteractionResult.CONSUME;
    }
}
