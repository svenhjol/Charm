package svenhjol.charm.mixin.feature.torchflowers_emit_light;

import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.torchflowers_emit_light.TorchflowersEmitLight;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourPropertiesMixin {
    @Inject(
        method = "getLightEmission",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetLightEmission(CallbackInfoReturnable<Integer> cir) {
        Resolve.feature(TorchflowersEmitLight.class).handlers
            .lightLevel((BlockBehaviour.BlockStateBase)(Object)this)
            .ifPresent(cir::setReturnValue);
    }
}
