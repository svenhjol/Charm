package svenhjol.charm.mixin.core;

import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.levelgen.feature.structures.SinglePoolElement;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructurePlaceSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.init.CharmDecorations;

@Mixin(SinglePoolElement.class)
public class AddCustomDecorationProcessorsMixin {
    /**
     * Adds all the structure processors defined in StructureHelper.SINGLE_POOL_ELEMENT_PROCESSORS
     * to the SinglePoolElement when the placement is being initialized.
     */
    @Inject(
        method = "getSettings",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;addProcessor(Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureProcessor;)Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructurePlaceSettings;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookCreatePlacementData(Rotation blockRotation, BoundingBox blockBox, boolean keepJigsaws, CallbackInfoReturnable<StructurePlaceSettings> cir, StructurePlaceSettings placement) {
        CharmDecorations.SINGLE_POOL_ELEMENT_PROCESSORS.forEach(placement::addProcessor);
    }
}
