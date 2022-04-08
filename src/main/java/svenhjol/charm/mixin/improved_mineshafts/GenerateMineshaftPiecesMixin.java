package svenhjol.charm.mixin.improved_mineshafts;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.improved_mineshafts.ImprovedMineshafts;

@Mixin(value = {
    MineshaftPieces.MineShaftCorridor.class,
    MineshaftPieces.MineShaftRoom.class
})
public class GenerateMineshaftPiecesMixin {
    /**
     * Once vanilla has rendered a piece, defer to {@link ImprovedMineshafts#generatePiece} to add more decoration.
     */
    @Inject(
        method = "postProcess",
        at = @At("TAIL")
    )
    private void hookGenerate(WorldGenLevel level, StructureManager structure, ChunkGenerator gen, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfo ci) {
        ImprovedMineshafts.generatePiece((StructurePiece) (Object) this, level, structure, gen, random, boundingBox, chunkPos, blockPos);
    }
}

