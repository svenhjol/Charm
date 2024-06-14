package svenhjol.charm.mixin.feature.torchflowers_emit_light;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.block.state.BlockBehaviour;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.torchflowers_emit_light.TorchflowersEmitLight;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourPropertiesMixin {

    @ModifyReturnValue(
            method = "getLightEmission",
            at = @At("RETURN")
    )
    private int hookGetLightEmission(int original) {
        return Resolve.feature(TorchflowersEmitLight.class).handlers
                .lightLevel((BlockBehaviour.BlockStateBase)(Object)this)
                .orElse(original);
    }
}
