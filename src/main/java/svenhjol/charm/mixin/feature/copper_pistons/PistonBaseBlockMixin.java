package svenhjol.charm.mixin.feature.copper_pistons;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Slice;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.copper_pistons.CopperPistons;

@SuppressWarnings({"UnreachableCode"})
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
            newState = Resolve.feature(CopperPistons.class).registers.copperPistonHeadBlock.get().withPropertiesOf(originalState);
        }

        return newState != null ? newState : originalState;
    }

    @ModifyReceiver(
            method = "triggerEvent",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private Block modifyMovingPiston(Block originalInstance) {
        Block newInstance = null;

        if (isCopperPistonBlock() || isStickyCopperPistonBlock()) {
            newInstance = Resolve.feature(CopperPistons.class).registers.movingCopperPistonBlock.get();
        }

        return newInstance != null ? newInstance : originalInstance;
    }

    @ModifyReturnValue(
            method = "getNeighborSignal",
            slice = @Slice(
                    from = @At(value = "INVOKE", target = "Lnet/minecraft/core/BlockPos;above()Lnet/minecraft/core/BlockPos;")
            ),
            at = @At(value = "RETURN")
    )
    private boolean hookReturnEarlyFromGetNeighbourSignal(boolean original) {
        if (isCopperPistonBlock() || isStickyCopperPistonBlock()) {
            return false;
        }
        return original;
    }

    @Unique
    private boolean isCopperPistonBlock() {
        var pistonBlockState = ((PistonBaseBlock)(Object)this).defaultBlockState();
        return pistonBlockState.is(Resolve.feature(CopperPistons.class).registers.copperPistonBlock.get());
    }

    @Unique
    private boolean isStickyCopperPistonBlock() {
        var pistonBlockState = ((PistonBaseBlock)(Object)this).defaultBlockState();
        return pistonBlockState.is(Resolve.feature(CopperPistons.class).registers.stickyCopperPistonBlock.get());
    }
}
