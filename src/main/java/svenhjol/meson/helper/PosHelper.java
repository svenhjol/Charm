package svenhjol.meson.helper;

import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.gen.feature.StructureFeature;

public class PosHelper {
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
}
