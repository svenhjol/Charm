package svenhjol.charm.helper;

import svenhjol.charm.Charm;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.material.Material;
import java.util.Random;

public class PosHelper {
    public static BlockPos addRandomOffset(BlockPos pos, Random rand, int min, int max) {
        int n = rand.nextInt(max - min) + min;
        int e = rand.nextInt(max - min) + min;
        int s = rand.nextInt(max - min) + min;
        int w = rand.nextInt(max - min) + min;
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

    public static boolean isInsideStructure(ServerLevel world, BlockPos pos, StructureFeature<?> structure) {
        return world.structureFeatureManager().getStructureAt(pos, true, structure).isValid();
    }

    public static boolean isLikeSolid(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return isSolid(world, pos) || state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.TOP_SNOW || state.getMaterial() == Material.CLAY || state.getMaterial() == Material.PLANT;
    }

    public static boolean isLikeAir(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return !state.canOcclude() || state.getMaterial() == Material.WATER || state.getMaterial() == Material.TOP_SNOW || state.getMaterial() == Material.PLANT || state.getMaterial() == Material.LEAVES || state.getMaterial() == Material.CLAY;
    }

    public static boolean isSolid(Level world, BlockPos pos) {
        BlockState state = world.getBlockState(pos);
        return state.canOcclude() && !world.isEmptyBlock(pos) && !state.getMaterial().isLiquid();
    }

    @Nullable
    public static BlockPos getSurfacePos(Level world, BlockPos pos) {
        int surface = 0;

        for (int y = world.getMaxBuildHeight(); y >= 0; --y) {
            BlockPos n = new BlockPos(pos.getX(), y, pos.getZ());
            if (world.isEmptyBlock(n) && !world.isEmptyBlock(n.below())) {
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
