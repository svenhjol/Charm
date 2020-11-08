package svenhjol.charm.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.PistonBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.base.CharmTags;

@Mixin(PistonBlock.class)
public class PistonBlockMixin {
    @Inject(
        method = "isMovable",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookIsMovable(BlockState blockState, World world, BlockPos blockPos, Direction direction, boolean canBreak, Direction pistonDir, CallbackInfoReturnable<Boolean> cir) {
        if (blockState.isIn(CharmTags.IMMOVABLE_BY_PISTONS))
            cir.setReturnValue(false);
    }
}
