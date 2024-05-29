package svenhjol.charm.mixin.feature.crop_feather_falling;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.FarmBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.crop_feather_falling.CropFeatherFalling;

@Mixin(FarmBlock.class)
public class FarmBlockMixin {
    @Inject(
        method = "fallOn",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookOnLandedUpon(Level level, BlockState state, BlockPos pos, Entity entity, float distance, CallbackInfo ci) {
        if (Resolve.feature(CropFeatherFalling.class).handlers.hasFeatherFalling(entity)) {
            ci.cancel();
        }
    }
}
