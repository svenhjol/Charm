package svenhjol.charm.world.decorator.outer;

import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.*;
import svenhjol.meson.decorator.MesonOuterDecorator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Trees extends MesonOuterDecorator
{
    public Trees(World world, BlockPos pos, Random rand, List<ChunkPos> chunks)
    {
        super(world, pos, rand, chunks);
    }

    @Override
    public void generate()
    {
        int max = 3 + rand.nextInt(4);

        List<WorldGenerator> generators = new ArrayList<>();
        generators.add(new WorldGenBigTree(false));
        generators.add(new WorldGenBirchTree(false, false));
        generators.add(new WorldGenTrees(false, 4 + rand.nextInt(7), Blocks.LOG.getDefaultState(), Blocks.LEAVES.getDefaultState(), true));
        generators.add(new WorldGenCanopyTree(false));

        for (int i = 0; i < max; i++) {
            int xx = rand.nextInt(16) + 8;
            int zz = rand.nextInt(16) + 8;
            WorldGenerator gen = generators.get(rand.nextInt(generators.size()));
            if (gen != null) {
                gen.generate(world, rand, world.getHeight(pos.add(xx, 0, zz)));
            }
        }
    }
}
