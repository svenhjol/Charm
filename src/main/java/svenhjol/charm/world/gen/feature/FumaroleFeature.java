package svenhjol.charm.world.gen.feature;

import com.mojang.datafixers.Dynamic;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import net.minecraft.world.gen.ChunkGenerator;
import net.minecraft.world.gen.GenerationSettings;
import net.minecraft.world.gen.feature.BlockClusterFeatureConfig;
import net.minecraft.world.gen.feature.Feature;
import svenhjol.charm.world.module.Fumaroles;

import java.util.Random;
import java.util.function.Function;

public class FumaroleFeature extends Feature<BlockClusterFeatureConfig> {
    public FumaroleFeature(Function<Dynamic<?>, ? extends BlockClusterFeatureConfig> config) {
        super(config);
    }

    @Override
    public boolean place(IWorld worldIn, ChunkGenerator<? extends GenerationSettings> generator, Random rand, BlockPos pos, BlockClusterFeatureConfig config) {
        for (int i = 0; i < 64; ++i) {
            BlockPos p = pos.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4), rand.nextInt(8) - rand.nextInt(8));
            if (worldIn.isAirBlock(p) && worldIn.getBlockState(p.down()).getBlock() == Blocks.NETHERRACK) {
                worldIn.setBlockState(p.down(), Fumaroles.block.getDefaultState(), 2);
            }
        }

        return true;
    }
}
