package svenhjol.charm.mixin.snow_accumulation;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.snow_accumulation.SnowAccumulation;

import java.util.Random;

@Mixin(SnowLayerBlock.class)
public class PreventSnowMeltingMixin {
    /**
     * Cancels snowblock randomtick melting of snow if snow accumulation is enabled.
     * @see SnowAccumulation#shouldAccumulateSnow(ServerLevel)
     */
    @Inject(
        method = "randomTick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookRandomTick(BlockState state, ServerLevel level, BlockPos pos, Random random, CallbackInfo ci) {
        if (SnowAccumulation.shouldAccumulateSnow(level)) {
            ci.cancel();
        }
    }
}
