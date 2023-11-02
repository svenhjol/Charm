package svenhjol.charm.mixin.torchflowers_emit_light;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TorchflowerCropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.torchflowers_emit_light.TorchflowersEmitLight;

@Mixin(BlockBehaviour.BlockStateBase.class)
public abstract class BlockBehaviourPropertiesMixin {
    @Unique
    private static int cachedLightLevel = -1;

    @Shadow public abstract boolean is(Block block);

    @Shadow protected abstract BlockState asState();

    @Inject(
        method = "getLightEmission",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookGetLightEmission(CallbackInfoReturnable<Integer> cir) {
        if (cachedLightLevel == -1) {
            cachedLightLevel = TorchflowersEmitLight.getLightLevel();
        }

        if (is(Blocks.TORCHFLOWER) || is(Blocks.POTTED_TORCHFLOWER)) {
            cir.setReturnValue(cachedLightLevel);
        }
        if (is(Blocks.TORCHFLOWER_CROP) && asState().getValue(TorchflowerCropBlock.AGE) == 1) {
            cir.setReturnValue(cachedLightLevel / 2);
        }
    }
}
