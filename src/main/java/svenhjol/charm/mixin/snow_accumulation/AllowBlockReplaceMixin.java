package svenhjol.charm.mixin.snow_accumulation;

import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.snow_accumulation.SnowAccumulation;

@Mixin(SnowLayerBlock.class)
public class AllowBlockReplaceMixin {
    @Inject(
        method = "canBeReplaced",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookCanBeReplaced(BlockState state, BlockPlaceContext context, CallbackInfoReturnable<Boolean> cir) {
        if (SnowAccumulation.allowBlockReplace) {
            int i = state.getValue(SnowLayerBlock.LAYERS);
            if (i < 8) {
                cir.setReturnValue(true);
            }
        }
    }
}
