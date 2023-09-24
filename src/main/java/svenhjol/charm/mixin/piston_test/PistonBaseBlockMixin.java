package svenhjol.charm.mixin.piston_test;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.piston_test.PistonTest;

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
        if (isCopperPistonBlock()) {
            var newState = PistonTest.copperPistonHeadBlock.get()
                .withPropertiesOf(originalState);
            return newState;
        }
        return originalState; // default behavior
    }

    /**
     * Vanilla behavior is to check the block above the piston for signal.
     * Copper pistons prevent that quasi-connectivity behaviour so redirect the pos to itself.
     */
    @Redirect(
        method = "getNeighborSignal",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/BlockPos;above()Lnet/minecraft/core/BlockPos;"
        )
    )
    private BlockPos redirectCheckAbove(BlockPos pos) {
        if (isCopperPistonBlock()) {
            return pos;
        }
        return pos.above(); // default behavior
    }

    @Unique
    private boolean isCopperPistonBlock() {
        var pistonBlockState = ((PistonBaseBlock)(Object)this).defaultBlockState();
        return pistonBlockState.is(PistonTest.copperPistonBlock.get());
    }
}
