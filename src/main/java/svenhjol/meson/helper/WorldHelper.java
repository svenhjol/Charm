package svenhjol.meson.helper;

import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.biome.Biome;
import svenhjol.meson.Meson;
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
        int offset = 1000;

        if (world.isRemote) return null;

        if (temples.contains(type)) {
            Biome biome;
            BlockPos templePos, tryPos;
            boolean found;
            int xx, zz;

            for (int dist = 0; dist < 50; dist++) {

                for (int x = -1; x <= 1; x++) {
                    for (int z = -1; z <= 1; z++) {

                        xx = x * offset + (x * dist * offset);
                        zz = z * offset + (z * dist * offset);
                        tryPos = chunk.add(xx, 0, zz);

                        templePos = getNearestStructure(world, tryPos, "Temple");
                        if (templePos == null) continue;

                        // because "Temple" can mean different things, check the biome they spawn in
                        biome = world.getBiome(templePos);
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

                        if (found) {
                            Meson.debug("WorldHelper: Found structure " + type + " at " + tryPos);
                            return templePos;
                        }
                    }
                }
            }
            Meson.debug("WorldHelper: Failed to find structure " + type);
            return null;
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
