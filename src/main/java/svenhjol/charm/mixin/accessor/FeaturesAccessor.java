package svenhjol.charm.mixin.accessor;

import net.minecraft.data.worldgen.Features;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Features.class)
public interface FeaturesAccessor {
    @Accessor("AZALEA_TREE")
    static ConfiguredFeature<TreeConfiguration, ?> getAzaleaTree() {
        throw new IllegalStateException();
    }

    @Accessor("AZALEA_TREE")
    static void setAzaleaTree(ConfiguredFeature<TreeConfiguration, ?> tree) {
        throw new IllegalStateException();
    }
}
