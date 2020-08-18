package svenhjol.charm.mixin;

import net.minecraft.block.FarmlandBlock;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.module.FeatherFallingCrops;

@Mixin(FarmlandBlock.class)
public class FarmlandBlockMixin {
    @Inject(method = "onLandedUpon", at = @At("HEAD"), cancellable = true)
    private void hookOnLandedUpon(World world, BlockPos pos, Entity entity, float distance, CallbackInfo ci) {
        if (FeatherFallingCrops.landedOnFarmlandBlock(entity))
            ci.cancel();
    }
}
