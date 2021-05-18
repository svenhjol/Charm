package svenhjol.charm.mixin.mineshaft_improvements;

import net.minecraft.structure.MineshaftGenerator;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.MineshaftImprovements;

import java.util.Random;

@Mixin(value = {
    MineshaftGenerator.MineshaftCorridor.class,
    MineshaftGenerator.MineshaftRoom.class
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
    private void hookGenerate(StructureWorldAccess world, StructureAccessor structure, ChunkGenerator gen, Random rand, BlockBox box, ChunkPos chunkPos, BlockPos blockPos, CallbackInfoReturnable<Boolean> cir) {
        if (cir.getReturnValue()) // if a piece did generate
            MineshaftImprovements.generatePiece((StructurePiece)(Object)this, world, structure, gen, rand, box, chunkPos, blockPos);
    }
}

