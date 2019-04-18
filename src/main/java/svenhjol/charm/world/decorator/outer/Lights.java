package svenhjol.charm.world.decorator.outer;

import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import svenhjol.charm.Charm;
import svenhjol.charm.crafting.feature.Lantern;
import svenhjol.meson.decorator.MesonOuterDecorator;

import java.util.List;
import java.util.Random;

public class Lights extends MesonOuterDecorator
{
    public Lights(World world, BlockPos pos, Random rand, List<ChunkPos> chunks)
    {
        super(world, pos, rand, chunks);
    }

    @Override
    public void generate()
    {
        int max = 1 + rand.nextInt(4);
        for (int i = 0; i < max; i++) {
            int xx = rand.nextInt(16) + 8;
            int zz = rand.nextInt(16) + 8;
            BlockPos posForLight = world.getHeight(pos.add(xx, 0, zz));
            boolean airAbove = world.getBlockState(posForLight) == Blocks.AIR.getDefaultState();
            boolean solidBelow = world.getBlockState(posForLight.offset(EnumFacing.DOWN)).isNormalCube();
            if (!airAbove || !solidBelow) continue;

            float f = rand.nextFloat();
            IBlockState light;

            if (f > 0.93f && Charm.hasFeature(Lantern.class)) {
                light = Lantern.getDefaultLantern().getDefaultState();
            } else if (f > 0.84f) {
                light = Blocks.REDSTONE_TORCH.getDefaultState();
            } else {
                light = Blocks.TORCH.getDefaultState();
            }

            world.setBlockState(posForLight, light);
        }
    }
}
