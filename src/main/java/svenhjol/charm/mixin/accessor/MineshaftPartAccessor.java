package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.feature.MineshaftFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(targets = {"net/minecraft/world/level/levelgen/structure/MineShaftPieces$MineShaftPiece"})
public interface MineshaftPartAccessor {
    @Accessor
    MineshaftFeature.Type getType();

    @Invoker
    boolean invokeIsSupportingBox(BlockGetter world, BoundingBox boundingBox, int minX, int maxX, int y, int z);
}
