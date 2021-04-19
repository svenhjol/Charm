package svenhjol.charm.mixin.accessor;

import net.minecraft.util.math.BlockBox;
import net.minecraft.world.BlockView;
import net.minecraft.world.gen.feature.MineshaftFeature;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = {"net/minecraft/structure/MineshaftGenerator$MineshaftPart"})
public interface MineshaftGeneratorAccessor {
    @Accessor
    MineshaftFeature.Type getMineshaftType();

    @Invoker
    boolean invokeIsSolidCeiling(BlockView world, BlockBox boundingBox, int minX, int maxX, int y, int z);
}
