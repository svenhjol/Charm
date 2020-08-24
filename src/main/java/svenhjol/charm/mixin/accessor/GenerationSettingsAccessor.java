package svenhjol.charm.mixin.accessor;

import net.minecraft.world.biome.GenerationSettings;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.util.List;
import java.util.function.Supplier;

@Mixin(GenerationSettings.class)
public interface GenerationSettingsAccessor {
    @Accessor
    List<Supplier<ConfiguredStructureFeature<?, ?>>> getStructureFeatures();

    @Accessor
    void setStructureFeatures(List<Supplier<ConfiguredStructureFeature<?, ?>>> structures);
}
