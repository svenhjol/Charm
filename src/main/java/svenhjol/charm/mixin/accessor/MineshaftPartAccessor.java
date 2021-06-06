package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.feature.MineshaftFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(targets = {"net/minecraft/structure/MineshaftGenerator$MineshaftPart"})
@CharmMixin(required = true)
public interface MineshaftPartAccessor {
    @Accessor
    MineshaftFeature.Type getMineshaftType();

    @Invoker
    boolean invokeIsSolidCeiling(BlockGetter world, BoundingBox boundingBox, int minX, int maxX, int y, int z);
}
