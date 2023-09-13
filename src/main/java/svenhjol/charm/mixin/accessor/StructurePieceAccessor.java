package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructurePiece.class)
public interface StructurePieceAccessor {
    @Invoker("getWorldX")
    int invokeGetWorldX(int x, int z);

    @Invoker("getWorldY")
    int invokeGetWorldY(int y);

    @Invoker("getWorldZ")
    int invokeGetWorldZ(int x, int z);

    @Invoker("placeBlock")
    void invokePlaceBlock(WorldGenLevel level, BlockState state, int x, int y, int z, BoundingBox box);
}
