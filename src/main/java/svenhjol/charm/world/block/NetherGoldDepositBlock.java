package svenhjol.charm.world.block;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import svenhjol.meson.MesonModule;
import svenhjol.meson.block.MesonBlock;

import java.util.Random;

public class NetherGoldDepositBlock extends MesonBlock {
    public NetherGoldDepositBlock(MesonModule module) {
        super(module, "nether_gold_deposit", Block.Properties.from(Blocks.NETHER_QUARTZ_ORE));
    }

    @Override
    public int getExpDrop(BlockState state, IWorldReader world, BlockPos pos, int fortune, int silktouch) {
        Random rand = world instanceof World ? ((World) world).rand : new Random();
        return rand.nextInt(2);
    }
}
