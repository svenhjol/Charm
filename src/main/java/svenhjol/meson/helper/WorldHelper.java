package svenhjol.meson.helper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import svenhjol.meson.iface.IMesonEnum;

public class WorldHelper
{
    public enum Structure implements IMesonEnum
    {
        buried_treasure,
        desert_pyramid,
        endcity,
        igloo,
        jungle_pyramid,
        mansion,
        mineshaft,
        fortress,
        monument,
        ocean_ruin,
        pillager_outpost,
        shipwreck,
        stronghold,
        swamp_hut,
        village;
    }

    public static BlockPos findNearestStructure(World world, Structure structure, BlockPos pos, int radius)
    {
        return world.findNearestStructure(WorldHelper.Structure.endcity.getName(), pos, radius, true);
    }

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
