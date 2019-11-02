package svenhjol.charm.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.Feature;
import net.minecraft.world.gen.feature.NoFeatureConfig;
import svenhjol.charm.world.module.Fumeroles;

import java.util.Random;
import java.util.function.Function;

public class FumeroleFeature extends Feature<NoFeatureConfig>
{
    public FumeroleFeature(Function<Dynamic<?>, ? extends NoFeatureConfig> config)
    {
        super(config);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, NoFeatureConfig config)
    {
        for (int i = 0; i < 64; ++i) {
            BlockPos p = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isAirBlock(p) && worldIn.getBlockState(p.down()).getBlock() == Blocks.NETHERRACK) {
                worldIn.setBlockState(p.down(), Fumeroles.block.getDefaultState(), 2);
            }
        }

        return true;
    }
}
