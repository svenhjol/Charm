package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(BiomeGenerationSettings.class)
public interface BiomeGenerationSettingsAccessor {
    @Mutable @Accessor
    List<Supplier<ConfiguredStructureFeature<?, ?>>> getStructureStarts();

    @Mutable @Accessor
    void setStructureStarts(List<Supplier<ConfiguredStructureFeature<?, ?>>> structures);

    @Mutable @Accessor
    List<List<Supplier<ConfiguredFeature<?, ?>>>> getFeatures();

    @Mutable @Accessor
    void setFeatures(List<List<Supplier<ConfiguredFeature<?, ?>>>> features);
}
