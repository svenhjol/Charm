package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(targets = {"net/minecraft/world/level/levelgen/structure/MineShaftPieces$MineShaftCorridor"})
@CharmMixin(required = true)
public interface MineshaftCorridorAccessor {
    @Invoker
    boolean invokeHasSturdyNeighbours(WorldGenLevel world, BoundingBox box, int x, int y, int z, int count);
}
