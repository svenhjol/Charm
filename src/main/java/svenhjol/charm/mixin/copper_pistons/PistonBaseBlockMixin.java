package svenhjol.charm.mixin.copper_pistons;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.SignalGetter;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.copper_pistons.CopperPistons;

@Mixin(PistonBaseBlock.class)
public class PistonBaseBlockMixin {
    /**
     * Change Blocks.PISTON_HEAD to COPPER_PISTON_HEAD when the piston base is a copper piston.
     * This sets the piston head to a copper piston head when it's finished extending.
     */
    @ModifyArg(
        method = "moveBlocks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/piston/MovingPistonBlock;newMovingBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;ZZ)Lnet/minecraft/world/level/block/entity/BlockEntity;",
            ordinal = 1
        ),
        index = 2
    )
    private BlockState modifyPistonHead(BlockState originalState) {
        BlockState newState = null;

        if (isCopperPistonBlock() || isStickyCopperPistonBlock()) {
            newState = CopperPistons.copperPistonHeadBlock.get().withPropertiesOf(originalState);
        }

        return newState != null ? newState : originalState;
    }

    @Inject(
        method = "getNeighborSignal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/BlockPos;above()Lnet/minecraft/core/BlockPos;",
            shift = At.Shift.BEFORE // Nudge back by 1 to avoid conflict with carpet
        ),
        cancellable = true
    )
    private void hookReturnEarlyFromGetNeighbourSignal(SignalGetter signalGetter, BlockPos blockPos, Direction direction, CallbackInfoReturnable<Boolean> cir) {
        if (isCopperPistonBlock() || isStickyCopperPistonBlock()) {
            cir.setReturnValue(false);
        }
    }

    @Unique
    private boolean isCopperPistonBlock() {
        var pistonBlockState = ((PistonBaseBlock)(Object)this).defaultBlockState();
        return pistonBlockState.is(CopperPistons.copperPistonBlock.get());
    }

    @Unique
    private boolean isStickyCopperPistonBlock() {
        var pistonBlockState = ((PistonBaseBlock)(Object)this).defaultBlockState();
        return pistonBlockState.is(CopperPistons.stickyCopperPistonBlock.get());
    }
}
