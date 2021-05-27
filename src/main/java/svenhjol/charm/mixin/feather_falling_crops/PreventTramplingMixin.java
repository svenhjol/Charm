package svenhjol.charm.mixin.feather_falling_crops;

import net.minecraft.block.BlockState;
import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.feather_falling_crops.FeatherFallingCrops;

@Mixin(FarmlandBlock.class)
public class PreventTramplingMixin {

    /**
     * Defer to landedOnFarmlandBlock.
     * If the check passes, return early from the vanilla method.
     */
    @Inject(method = "onLandedUpon", at = @At("HEAD"), cancellable = true)
    private void hookOnLandedUpon(World world, BlockState state, BlockPos pos, Entity entity, float distance, CallbackInfo ci) {
        if (FeatherFallingCrops.landedOnFarmlandBlock(entity))
            ci.cancel();
    }
}
