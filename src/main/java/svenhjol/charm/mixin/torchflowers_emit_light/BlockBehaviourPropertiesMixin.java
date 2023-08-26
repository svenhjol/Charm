package svenhjol.charm.mixin.torchflowers_emit_light;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.torchflowers_emit_light.TorchflowersEmitLight;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourPropertiesMixin {
    @Shadow public abstract boolean is(Block block);

    @Inject(
        method = "getLightEmission",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetLightEmission(CallbackInfoReturnable<Integer> cir) {
        if (is(Blocks.TORCHFLOWER)) {
            cir.setReturnValue(TorchflowersEmitLight.lightLevel);
        }
    }
}
