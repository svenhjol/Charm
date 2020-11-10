package svenhjol.charm.base.helper;

import net.minecraft.block.BlockState;
import net.minecraft.block.Material;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.gen.feature.StructureFeature;
import svenhjol.charm.Charm;

import javax.annotation.Nullable;
import java.util.Random;

public class PosHelper {
    public static BlockPos addRandomOffset(BlockPos pos, Random rand, int max) {
        int n = rand.nextInt(max);
        int e = rand.nextInt(max);
        int s = rand.nextInt(max);
        int w = rand.nextInt(max);
        pos = pos.north(rand.nextFloat() < 0.5F ? n : -n);
        pos = pos.east(rand.nextFloat() < 0.5F ? e : -e);
        pos = pos.south(rand.nextFloat() < 0.5F ? s : -s);
        pos = pos.west(rand.nextFloat() < 0.5F ? w : -w);
        return pos;
    }

    public static double getDistanceSquared(BlockPos pos1, BlockPos pos2) {
        double d0 = pos1.getX();
        double d1 = pos1.getZ();
        double d2 = d0 - pos2.getX();
        double d3 = d1 - pos2.getZ();
        return d2 * d2 + d3 * d3;
    }

    public static boolean isInsideStructure(ServerWorld world, BlockPos pos, StructureFeature<?> structure) {
        return world.getStructureAccessor().getStructureAt(pos, true, structure).hasChildren();
    }

    public static boolean isLikeSolid(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return isSolid(world, pos) || state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.SNOW_LAYER || state.getMaterial() == Material.ORGANIC_PRODUCT || state.getMaterial() == Material.PLANT;
    }

    public static boolean isLikeAir(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return !state.isOpaque() || state.getMaterial() == Material.WATER || state.getMaterial() == Material.SNOW_LAYER || state.getMaterial() == Material.PLANT || state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.ORGANIC_PRODUCT;
    }

    public static boolean isSolid(World world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.isOpaque() && !world.isAir(pos) && !state.getMaterial().isLiquid();
    }

    @Nullable
    public static BlockPos getSurfacePos(World world, BlockPos pos) {
        int surface = 0;

        for (int y = world.getHeight(); y >= 0; --y) {
            BlockPos n = new BlockPos(pos.getX(), y, pos.getZ());
            if (world.isAir(n) && !world.isAir(n.down())) {
                surface = y;
                break;
            }
        }

        if (surface <= 0) {
            Charm.LOG.warn("Failed to find a surface value to spawn the player");
            return null;
        }

        return new BlockPos(pos.getX(), surface, pos.getZ());
    }
}
