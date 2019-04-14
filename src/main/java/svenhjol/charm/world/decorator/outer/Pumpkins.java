package svenhjol.charm.world.decorator.outer;

import net.minecraft.block.BlockPumpkin;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import svenhjol.charm.world.feature.VillageDecorations;
import svenhjol.meson.decorator.MesonOuterDecorator;

import java.util.List;
import java.util.Random;

public class Pumpkins extends MesonOuterDecorator
{
    public Pumpkins(World world, BlockPos pos, Random rand, List<ChunkPos> chunks)
    {
        super(world, pos, rand, chunks);
    }

    @Override
    public void generate()
    {
        int max = 3;
        for (int i = 0; i < max; i++) {
            if (rand.nextFloat() < VillageDecorations.pumpkinsWeight) continue;

            int xx = rand.nextInt(16) + 8;
            int zz = rand.nextInt(16) + 8;

            BlockPos current = world.getHeight(pos.add(xx, 0, zz));
            boolean airAbove = world.getBlockState(current) == Blocks.AIR.getDefaultState();
            boolean grassBelow = world.getBlockState(current.offset(EnumFacing.DOWN)) == Blocks.GRASS.getDefaultState();
            if (!airAbove || !grassBelow) continue;

            IBlockState state;
            float r = rand.nextFloat();

            if (r <= 0.05) {
                state = Blocks.MELON_BLOCK.getDefaultState();
            } else if (r <= 0.3) {
                state = Blocks.LIT_PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.byHorizontalIndex(rand.nextInt(4)));
            } else {
                state = Blocks.PUMPKIN.getDefaultState().withProperty(BlockPumpkin.FACING, EnumFacing.byHorizontalIndex(rand.nextInt(4)));
            }

            world.setBlockState(current, state);
        }
    }
}
