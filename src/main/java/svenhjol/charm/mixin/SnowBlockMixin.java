package svenhjol.charm.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.SnowBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.SnowStorms;

import java.util.Random;

@Mixin(SnowBlock.class)
public class SnowBlockMixin {
    /**
     * Cancels snowblock randomtick melting of snow if there's a storm in progress.
     * @see {{@link SnowStorms#tryRandomTick(ServerWorld)}}
     */
    @Inject(
        method = "randomTick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookRandomTick(BlockState state, ServerWorld world, BlockPos pos, Random random, CallbackInfo ci) {
        if (SnowStorms.tryRandomTick(world))
            ci.cancel();
    }
}
