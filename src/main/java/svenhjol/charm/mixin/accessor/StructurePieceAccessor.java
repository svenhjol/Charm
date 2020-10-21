package svenhjol.charm.mixin.accessor;

import net.minecraft.block.BlockState;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.world.StructureWorldAccess;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(StructurePiece.class)
public interface StructurePieceAccessor {
    @Accessor()
    BlockBox getBoundingBox();

    @Invoker
    void callAddBlock(StructureWorldAccess worldIn, BlockState blockstateIn, int x, int y, int z, BlockBox boundingboxIn);

    @Invoker
    int callApplyXTransform(int x, int z);

    @Invoker
    int callApplyYTransform(int y);

    @Invoker
    int callApplyZTransform(int x, int z);
}
