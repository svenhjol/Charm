package svenhjol.charm.mixin.mineshaft_improvements;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.mineshaft_improvements.MineshaftImprovements;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.MineShaftPieces;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

@Mixin(value = {
    MineShaftPieces.MineShaftCorridor.class,
    MineShaftPieces.MineShaftRoom.class
})
public class GenerateMineshaftPiecesMixin {

    /**
     * Defers to the generatePiece method if a vanilla mineshaft piece generated.
     * This allows the MineshaftImprovements module to hack at the piece.
     */
    @Inject(
        method = "generate(Lnet/minecraft/world/StructureWorldAccess;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/chunk/ChunkGenerator;Ljava/util/Random;Lnet/minecraft/util/math/BlockBox;Lnet/minecraft/util/math/ChunkPos;Lnet/minecraft/util/math/BlockPos;)Z",
        at = @At("RETURN")
    )
    private void hookGenerate(WorldGenLevel world, StructureFeatureManager structure, ChunkGenerator gen, Random rand, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) // if a piece did generate
            MineshaftImprovements.generatePiece((StructurePiece)(Object)this, world, structure, gen, rand, box, chunkPos, blockPos);
    }
}

