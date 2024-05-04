package svenhjol.charm.feature.deepslate_dungeons;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public final class CommonCallbacks {
    public static BlockState changeBlockState(WorldGenLevel level, BlockPos pos, BlockState state) {
        if (level.getMinBuildHeight() < 0 && pos.getY() < 0) {
            var random = level.getRandom();

            if (state.getBlock() == Blocks.COBBLESTONE) {
                return random.nextBoolean() ? Blocks.DEEPSLATE_BRICKS.defaultBlockState() : Blocks.CRACKED_DEEPSLATE_BRICKS.defaultBlockState();
            } else if (state.getBlock() == Blocks.MOSSY_COBBLESTONE) {
                return Blocks.COBBLED_DEEPSLATE.defaultBlockState();
            }
        }

        return state;
    }
}
