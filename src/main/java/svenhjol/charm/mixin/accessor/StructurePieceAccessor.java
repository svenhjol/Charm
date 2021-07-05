package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructurePiece.class)
public interface StructurePieceAccessor {
    @Accessor()
    BoundingBox getBoundingBox();

    @Invoker
    void invokePlaceBlock(WorldGenLevel worldIn, BlockState blockstateIn, int x, int y, int z, BoundingBox boundingboxIn);

    @Invoker
    int invokeGetWorldX(int x, int z);

    @Invoker
    int invokeGetWorldY(int y);

    @Invoker
    int invokeGetWorldZ(int x, int z);
}
