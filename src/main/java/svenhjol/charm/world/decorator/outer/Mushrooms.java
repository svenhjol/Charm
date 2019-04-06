package svenhjol.charm.world.decorator.outer;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.WorldGenBigMushroom;
import net.minecraft.world.gen.feature.WorldGenerator;
import svenhjol.meson.decorator.MesonOuterDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Mushrooms extends MesonOuterDecorator
{
    public Mushrooms(World world, BlockPos pos, Random rand, List<ChunkPos> chunks)
    {
        super(world, pos, rand, chunks);
    }

    @Override
    public void generate()
    {
        List<Block> mushrooms = new ArrayList<>();
        int max = 5 + rand.nextInt(10);

        mushrooms.add(Blocks.BROWN_MUSHROOM);
        mushrooms.add(Blocks.RED_MUSHROOM);
        WorldGenerator bigMushroom = new WorldGenBigMushroom();

        for (int i = 0; i < max; i++) {
            int xx = rand.nextInt(16) + 8;
            int zz = rand.nextInt(16) + 8;
            BlockPos forMushroom = world.getHeight(pos.add(xx, 0, zz));
            boolean airAbove = world.getBlockState(forMushroom) == Blocks.AIR.getDefaultState();
            boolean grassBelow = world.getBlockState(forMushroom.offset(EnumFacing.DOWN)) == Blocks.GRASS.getDefaultState();
            if (!airAbove || !grassBelow) continue;

            if (rand.nextFloat() < 0.75f) {
                world.setBlockState(forMushroom, mushrooms.get(rand.nextInt(mushrooms.size())).getDefaultState());
            }

            if (rand.nextFloat() < 0.75f) {
                bigMushroom.generate(world, rand, forMushroom);
            }
        }
    }
}
