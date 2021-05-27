package svenhjol.charm.mixin.accessor;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;
import svenhjol.charm.annotation.CharmMixin;

import java.util.List;
import java.util.function.Supplier;

@Mixin(GenerationSettings.class)
@CharmMixin(required = true)
public interface GenerationSettingsAccessor {
    @Mutable
    @Accessor
    List<Supplier<ConfiguredStructureFeature<?, ?>>> getStructureFeatures();

    @Mutable
    @Accessor
    void setStructureFeatures(List<Supplier<ConfiguredStructureFeature<?, ?>>> structures);

    @Mutable
    @Accessor
    List<List<Supplier<ConfiguredFeature<?, ?>>>> getFeatures();

    @Mutable
    @Accessor
    void setFeatures(List<List<Supplier<ConfiguredFeature<?, ?>>>> features);
}
