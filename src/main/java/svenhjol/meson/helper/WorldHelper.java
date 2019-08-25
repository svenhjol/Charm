package svenhjol.meson.helper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

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

    public static Biome getBiomeAtPos(World world, BlockPos pos)
    {
        // world.getBiome() suffers from infinite badness when game loading
        return world.getChunkProvider().getChunkGenerator().getBiomeProvider().getBiome(pos);
    }

    public static int getDimensionId(World world)
    {
        return world.dimension.getType().getId();
    }
}
