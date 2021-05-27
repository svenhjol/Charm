package svenhjol.charm.mixin.core;

import net.minecraft.structure.StructurePlacementData;
import net.minecraft.structure.pool.SinglePoolElement;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.init.CharmDecoration;
import svenhjol.charm.annotation.CharmMixin;

@Mixin(SinglePoolElement.class)
@CharmMixin(required = true)
public class AddCustomDecorationProcessorsMixin {

    /**
     * Adds all the structure processors defined in StructureHelper.SINGLE_POOL_ELEMENT_PROCESSORS
     * to the SinglePoolElement when the placement is being initialized.
     */
    @Inject(
        method = "createPlacementData",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/structure/StructurePlacementData;addProcessor(Lnet/minecraft/structure/processor/StructureProcessor;)Lnet/minecraft/structure/StructurePlacementData;"
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookCreatePlacementData(BlockRotation blockRotation, BlockBox blockBox, boolean keepJigsaws, CallbackInfoReturnable<StructurePlacementData> cir, StructurePlacementData placement) {
        CharmDecoration.SINGLE_POOL_ELEMENT_PROCESSORS.forEach(placement::addProcessor);
    }
}
