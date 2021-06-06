package svenhjol.charm.mixin.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.init.CharmTags;

@Mixin(PistonBaseBlock.class)
public class CheckImmovableByPistonMixin {
    /**
     * When checking if a block can be moved by a piston, also check
     * Charm's IMMOVABLE_BY_PISTONS tag. If the block is in the tag,
     * then return false early so that the piston does not move it.
     */
    @Inject(
        method = "isPushable",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookIsMovable(BlockState blockState, Level world, BlockPos blockPos, Direction direction, boolean canBreak, Direction pistonDir, CallbackInfoReturnable<Boolean> cir) {
        if (blockState.is(CharmTags.IMMOVABLE_BY_PISTONS))
            cir.setReturnValue(false);
    }
}
