package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MineshaftPieces.MineShaftCorridor.class)
public interface MineshaftCorridorAccessor {
    @Invoker("hasSturdyNeighbours")
    boolean invokeHasSturdyNeighbours(WorldGenLevel level, BoundingBox box, int a, int b, int c, int d);
}
