package svenhjol.charm.mixin.accessor;

import net.minecraft.world.gen.feature.TreeFeatureConfig;
import net.minecraft.world.gen.stateprovider.BlockStateProvider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TreeFeatureConfig.class)
public interface TreeFeatureConfigAccessor {
    @Mutable
    @Accessor
    void setTrunkProvider(BlockStateProvider provider);
}
