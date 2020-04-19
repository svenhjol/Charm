package svenhjol.meson.helper;

import net.minecraft.block.BlockState;
import net.minecraft.block.CauldronBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;

public class CauldronHelper {
    public static boolean isFull(BlockState state) {
        return state.getProperties().contains(CauldronBlock.LEVEL) && state.get(CauldronBlock.LEVEL) == 3;
    }

    public static boolean hasWater(BlockState state) {
        return state.getProperties().contains(CauldronBlock.LEVEL) && state.get(CauldronBlock.LEVEL) > 0;
    }

    public static void clearCauldron(IWorld world, BlockPos pos, BlockState state) {
        world.setBlockState(pos, state.with(CauldronBlock.LEVEL, 0), 2);
    }
}
