package svenhjol.meson.helper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class WorldHelper
{

    @SuppressWarnings("unused")
    public static BlockPos getNearestStronghold(World world, BlockPos chunk)
    {
        return getNearestStructure(world, chunk, "Stronghold");
    }

    public static BlockPos getNearestVillage(World world, BlockPos chunk)
    {
        return getNearestStructure(world, chunk, "Village");
    }

    public static BlockPos getNearestStructure(World world, BlockPos chunk, String structure)
    {
        return ((WorldServer) world).getChunkProvider().getNearestStructurePos(world, structure, chunk, false);
    }

    public static long getNearestVillageSeed(World world, BlockPos chunk)
    {
        BlockPos nearest = getNearestVillage(world, chunk);
        long seed = 0;

        if (nearest != null) {
            seed = nearest.toString().hashCode();
        }

        return seed;
    }

    public static double getDistanceSq(BlockPos pos1, BlockPos pos2)
    {
        double d0 = (double)(pos1.getX());
        double d1 = (double)(pos1.getZ());
        double d2 = d0 - pos2.getX();
        double d3 = d1 - pos2.getZ();
        return d2 * d2 + d3 * d3;
    }

    public static ChunkPos getChunkPos(BlockPos pos)
    {
        return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
    }
}
