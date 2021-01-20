package svenhjol.charm.mixin;

import net.minecraft.structure.Structure;
import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.processor.GravityStructureProcessor;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.Heightmap;
import net.minecraft.world.WorldView;
import net.minecraft.world.biome.Biome;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GravityStructureProcessor.class)
public class GravityStructureProcessorMixin {
    private static final ThreadLocal<Biome.Category> biomeCategory = new ThreadLocal<>();

    @Inject(
        method = "process",
        at = @At("HEAD")
    )
    private void hookProcess(WorldView worldView, BlockPos pos, BlockPos blockPos, Structure.StructureBlockInfo structureBlockInfo, Structure.StructureBlockInfo structureBlockInfo2, StructurePlacementData structurePlacementData, CallbackInfoReturnable<Structure.StructureBlockInfo> cir) {
        biomeCategory.set(worldView.getBiome(pos).getCategory());
    }

    @ModifyArg(
        method = "process",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/WorldView;getTopY(Lnet/minecraft/world/Heightmap$Type;II)I"
        ),
        index = 0
    )
    private Heightmap.Type hookProcess(Heightmap.Type in) {
        Biome.Category category = biomeCategory.get();
        if (category == Biome.Category.OCEAN && in == Heightmap.Type.WORLD_SURFACE_WG) {
            return Heightmap.Type.OCEAN_FLOOR_WG;
        }

        return in;
    }
}
