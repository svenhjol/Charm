package svenhjol.meson.helper;

import net.minecraft.world.biome.Biome;

import java.util.Objects;

public class BiomeHelper
{
    public static String getBiomeName(Biome biome)
    {
        if (biome == null) return "";
        return Objects.requireNonNull(biome.getRegistryName()).getPath();
    }
}
