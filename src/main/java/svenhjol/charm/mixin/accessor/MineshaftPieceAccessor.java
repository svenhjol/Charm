package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MineshaftPieces.MineShaftPiece.class)
public interface MineshaftPieceAccessor {
    @Accessor("type")
    MineshaftStructure.Type getType();

    @Invoker("isSupportingBox")
    boolean invokeIsSupportingBox(BlockGetter level, BoundingBox box, int a, int b, int c, int d);
}
