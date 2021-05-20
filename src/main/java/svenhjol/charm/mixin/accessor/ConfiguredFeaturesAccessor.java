package svenhjol.charm.mixin.accessor;

import net.minecraft.world.gen.feature.ConfiguredFeature;
import net.minecraft.world.gen.feature.ConfiguredFeatures;
import net.minecraft.world.gen.feature.TreeFeatureConfig;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ConfiguredFeatures.class)
public interface ConfiguredFeaturesAccessor {
    @Accessor("AZALEA_TREE")
    static ConfiguredFeature<TreeFeatureConfig, ?> getAzaleaTree() {
        throw new IllegalStateException();
    }

    @Accessor("AZALEA_TREE")
    static void setAzaleaTree(ConfiguredFeature<TreeFeatureConfig, ?> tree) {
        throw new IllegalStateException();
    }
}
