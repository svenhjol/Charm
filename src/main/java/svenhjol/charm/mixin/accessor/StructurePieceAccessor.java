package svenhjol.charm.mixin.accessor;

import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(StructurePiece.class)
@CharmMixin(required = true)
public interface StructurePieceAccessor {
    @Accessor()
    BoundingBox getBoundingBox();

    @Invoker
    void callAddBlock(WorldGenLevel worldIn, BlockState blockstateIn, int x, int y, int z, BoundingBox boundingboxIn);

    @Invoker
    int callApplyXTransform(int x, int z);

    @Invoker
    int callApplyYTransform(int y);

    @Invoker
    int callApplyZTransform(int x, int z);
}
