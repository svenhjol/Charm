package svenhjol.charm.mixin.no_crop_trampling;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.no_crop_trampling.NoCropTrampling;

@Mixin(FarmBlock.class)
public class PreventTramplingMixin {
    /**
     * Defer to {@link NoCropTrampling#shouldNotTrample}.
     * If the check passes, return early from the vanilla method.
     */
    @Inject(method = "fallOn", at = @At("HEAD"), cancellable = true)
    private void hookOnLandedUpon(Level level, BlockState state, BlockPos pos, Entity entity, float distance, CallbackInfo ci) {
        if (NoCropTrampling.shouldNotTrample(entity)) {
            ci.cancel();
        }
    }
}
