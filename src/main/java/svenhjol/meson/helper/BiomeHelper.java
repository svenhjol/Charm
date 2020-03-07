package svenhjol.meson.helper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.Objects;

public class BiomeHelper
{
    public static String getBiomeName(Biome biome)
    {
        if (biome == null) return "";
        return Objects.requireNonNull(biome.getRegistryName()).getPath();
    }

    public static Biome getBiomeAtPos(World world, BlockPos pos)
    {
        // world.getBiome() suffers from infinite badness when game loading
        return world.getChunkProvider().getChunkGenerator().getBiomeProvider().getBiome(pos); // [1.14]
        // return world.getChunkProvider().getChunkGenerator().getBiomeProvider().getNoiseBiome(pos.getX(), pos.getY(), pos.getZ()); // [1.15]
    }
}
