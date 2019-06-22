package svenhjol.meson.helper;

import net.minecraft.init.Biomes;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import svenhjol.meson.iface.IMesonEnum;

import java.util.ArrayList;
import java.util.List;

public class WorldHelper
{
    public enum StructureType implements IMesonEnum
    {
        VILLAGE,
        MINESHAFT,
        SWAMP_HUT,
        DESERT_PYRAMID,
        JUNGLE_PYRAMID,
        IGLOO
    }

    public static List<StructureType> temples = new ArrayList<>();

    static {
        temples.add(StructureType.SWAMP_HUT);
        temples.add(StructureType.DESERT_PYRAMID);
        temples.add(StructureType.JUNGLE_PYRAMID);
        temples.add(StructureType.IGLOO);
    }

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

    public static BlockPos getNearestStructure(World world, BlockPos chunk, StructureType type)
    {
        if (temples.contains(type)) {
            Biome biome;
            BlockPos temple = null;
            boolean found = false;

            for (int dist = 0; dist < 10; dist++) {
                for (int face = 2; face < 6; face++) {
                    temple = getNearestStructure(world, chunk.offset(EnumFacing.byIndex(face), dist * 200), "Temple");
                    if (temple == null) continue;

                    biome = world.getBiome(temple);
                    switch (type) {
                        case SWAMP_HUT:
                            found = (biome == Biomes.SWAMPLAND);
                            break;
                        case IGLOO:
                            found = (biome == Biomes.ICE_PLAINS || biome == Biomes.COLD_TAIGA);
                            break;
                        case DESERT_PYRAMID:
                            found = (biome == Biomes.DESERT || biome == Biomes.DESERT_HILLS);
                            break;
                        case JUNGLE_PYRAMID:
                            found = (biome == Biomes.JUNGLE || biome == Biomes.JUNGLE_HILLS);
                            break;
                        default:
                            found = true;
                            break;
                    }

                    if (found) break;
                }
            }
            return found ? temple : null;
        } else {
            return getNearestStructure(world, chunk, type.getCapitalizedName());
        }
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

    public static void searchPos(World world, BlockPos pos, int dist, String structure)
    {

    }

    public static ChunkPos getChunkPos(BlockPos pos)
    {
        return new ChunkPos(pos.getX() >> 4, pos.getZ() >> 4);
    }
}
