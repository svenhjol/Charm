package svenhjol.charm.mixin.feather_falling_crops;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.feather_falling_crops.FeatherFallingCrops;

@Mixin(FarmBlock.class)
public class PreventTramplingMixin {

    /**
     * Defer to landedOnFarmlandBlock.
     * If the check passes, return early from the vanilla method.
     */
    @Inject(method = "onLandedUpon", at = @At("HEAD"), cancellable = true)
    private void hookOnLandedUpon(Level world, BlockState state, BlockPos pos, Entity entity, float distance, CallbackInfo ci) {
        if (FeatherFallingCrops.landedOnFarmlandBlock(entity))
            ci.cancel();
    }
}
