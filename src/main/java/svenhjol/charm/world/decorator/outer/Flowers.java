package svenhjol.charm.world.decorator.outer;

import net.minecraft.block.BlockFlower;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.feature.*;
import svenhjol.meson.decorator.MesonOuterDecorator;

import java.util.*;

public class Flowers extends MesonOuterDecorator
{
    public Flowers(World world, BlockPos pos, Random rand, List<ChunkPos> chunks)
    {
        super(world, pos, rand, chunks);
    }

    @Override
    public void generate()
    {
        Biome biome = world.getBiome(pos);
        List<WorldGenerator> generators = new ArrayList<>();

        if (biome.getDefaultTemperature() > 1.0) {
            // desert and hot
            generators.add(new WorldGenDeadBush());
        } else if (biome == Biomes.SWAMPLAND || biome == Biomes.MUTATED_SWAMPLAND) {
            // swamps
            generators.add(new WorldGenBigMushroom());
            generators.add(new WorldGenWaterlily());
        }

        // select three flowers from the village-seeded rand
        List<BlockFlower.EnumFlowerType> flowerTypes = Arrays.asList(BlockFlower.EnumFlowerType.values());
        Collections.shuffle(flowerTypes, rand);

        for (BlockFlower.EnumFlowerType flower : flowerTypes.subList(0, 2)) {
            BlockFlower block = flower == BlockFlower.EnumFlowerType.DANDELION ? Blocks.YELLOW_FLOWER : Blocks.RED_FLOWER;
            generators.add(new WorldGenFlowers(block, flower));
        }

        generators.add(new WorldGenReed());

        // decorate using co-ord specific gen
        int max = 2 + rand.nextInt(4);

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
