package svenhjol.charm.world.decorator.outer;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import svenhjol.meson.decorator.MesonOuterDecorator;

import java.util.List;
import java.util.Random;

public class Crops extends MesonOuterDecorator
{
    public Crops(World world, BlockPos pos, Random rand, List<ChunkPos> chunks)
    {
        super(world, pos, rand, chunks);
    }

    @Override
    public void generate()
    {
        int max = 64;
        for (int i = 0; i < max; i++) {
            int xx = rand.nextInt(16) + 8;
            int zz = rand.nextInt(16) + 8;

            BlockPos current = world.getHeight(pos.add(xx, 0, zz));
            boolean airAbove = world.getBlockState(current) == Blocks.AIR.getDefaultState();
            boolean grassBelow = world.getBlockState(current.offset(EnumFacing.DOWN)) == Blocks.GRASS.getDefaultState();

            if (!airAbove || !grassBelow) continue;

            if (rand.nextFloat() < 0.15f) {
                if (!world.getBlockState(current.offset(EnumFacing.DOWN, 2)).isNormalCube()) continue;
                if (!world.getBlockState(current.offset(EnumFacing.NORTH).offset(EnumFacing.DOWN, 1)).isNormalCube()) continue;
                if (!world.getBlockState(current.offset(EnumFacing.EAST).offset(EnumFacing.DOWN, 1)).isNormalCube()) continue;
                if (!world.getBlockState(current.offset(EnumFacing.SOUTH).offset(EnumFacing.DOWN, 1)).isNormalCube()) continue;
                if (!world.getBlockState(current.offset(EnumFacing.WEST).offset(EnumFacing.DOWN, 1)).isNormalCube()) continue;

                world.setBlockState(current.offset(EnumFacing.DOWN, 1), Blocks.WATER.getDefaultState(), 2);
                world.setBlockState(current, Blocks.TRAPDOOR.getDefaultState().withProperty(BlockTrapDoor.OPEN, false), 2);
                continue;
            }

            if (rand.nextFloat() < 0.85f) {
                IBlockState farmland = Blocks.FARMLAND.getDefaultState().withProperty(BlockFarmland.MOISTURE, 6 + rand.nextInt(2));
                world.setBlockState(current.offset(EnumFacing.DOWN), farmland);
            }

            IBlockState crop = null;
            int type = rand.nextInt(8);

            if (type == 0) {
                crop = ((BlockPotato) Blocks.POTATOES).withAge(6 + rand.nextInt(1));
            } else if (type == 1) {
                crop = ((BlockCarrot) Blocks.CARROTS).withAge(6 + rand.nextInt(1));
            } else if (type == 2) {
                crop = ((BlockBeetroot) Blocks.BEETROOTS).withAge(3);
            } else if (type <= 6) {
                crop = ((BlockCrops) Blocks.WHEAT).withAge(6 + rand.nextInt(1));
            }

            if (crop != null) {
                world.setBlockState(current, crop);
            }
        }
    }
}
