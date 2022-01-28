package svenhjol.charm.mixin.improved_mineshafts;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.MineShaftPieces;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.improved_mineshafts.ImprovedMineshafts;

import java.util.Random;

@Mixin(value = {
    MineShaftPieces.MineShaftCorridor.class,
    MineShaftPieces.MineShaftRoom.class
})
public class GenerateMineshaftPiecesMixin {
    /**
     * Once vanilla has rendered a piece, defer to {@link ImprovedMineshafts#generatePiece} to add more decoration.
     */
    @Inject(
        method = "postProcess",
        at = @At("TAIL")
    )
    private void hookGenerate(WorldGenLevel level, StructureFeatureManager structure, ChunkGenerator gen, Random random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos, CallbackInfo ci) {
        ImprovedMineshafts.generatePiece((StructurePiece) (Object) this, level, structure, gen, random, boundingBox, chunkPos, blockPos);
    }
}

