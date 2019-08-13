package svenhjol.meson.helper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import svenhjol.charm.world.feature.StructureMaps.StructureType;

public class WorldHelper
{
    public static double getDistanceSq(BlockPos pos1, BlockPos pos2)
    {
        double d0 = pos1.getX();
        double d1 = pos1.getZ();
        double d2 = d0 - pos2.getX();
        double d3 = d1 - pos2.getZ();
        return d2 * d2 + d3 * d3;
    }

    public static BlockPos getNearestStructure(World world, BlockPos pos, StructureType type)
    {
        if (world.isRemote) return null; // prevent villager trade crashes
        return ((ServerWorld)world).getChunkProvider().generator.findNearestStructure(world, type.getCapitalizedName(), pos, 1000, false);
    }
}
