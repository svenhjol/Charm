package svenhjol.charm.mixin.snow_storms;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.snow_storms.SnowStorms;

import java.util.Random;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.SnowLayerBlock;
import net.minecraft.world.level.block.state.BlockState;

@Mixin(SnowLayerBlock.class)
public class PreventSnowMeltingMixin {

    /**
     * Cancels snowblock randomtick melting of snow if there's a storm in progress.
     * @see SnowStorms#tryRandomTick
     */
    @Inject(
        method = "randomTick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookRandomTick(BlockState state, ServerLevel world, BlockPos pos, Random random, CallbackInfo ci) {
        if (SnowStorms.tryRandomTick(world))
            ci.cancel();
    }
}
