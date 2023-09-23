package svenhjol.charm.mixin.piston_test;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
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
    @Redirect(
        method = "triggerEvent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z",
            ordinal = 2
        )
    )
    private boolean redirectMovingPistonBlockState(Level level, BlockPos pos, BlockState originalState, int mode) {
        if (isCopperPistonBlock()) {
            var opt = PistonTest.tryMapState(originalState);
            if (opt.isPresent()) {
                return level.setBlock(pos, opt.get(), mode);
            }
        }
        return level.setBlock(pos, originalState, mode);
    }

    /**
     * Line 191 Set second argument of newMovingBlockEntity
     * from Blocks.MOVING_PISTON to MOVING_COPPER_PISTON
     */
    @ModifyArg(
        method = "triggerEvent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/piston/MovingPistonBlock;newMovingBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;ZZ)Lnet/minecraft/world/level/block/entity/BlockEntity;",
            ordinal = 0
        ),
        index = 1
    )
    private BlockState modifyRetractingPistonHead(BlockState originalState) {
        if (isCopperPistonBlock()) {
            return PistonTest.movingCopperPistonBlock.get().withPropertiesOf(originalState);
        }
        return originalState;
    }

    /**
     * Line 192 Set second argument of blockUpdated
     * from Blocks.MOVING_PISTON to MOVING_COPPER_PISTON
     */
    @ModifyArg(
        method = "triggerEvent",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;blockUpdated(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;)V"
        ),
        index = 1
    )
    private Block modifyBlockUpdated(Block originalBlock) {
        if (isCopperPistonBlock()) {
            return PistonTest.movingCopperPistonBlock.get();
        }
        return originalBlock;
    }

    @Redirect(
        method = "moveBlocks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
        )
    )
    private boolean redirectBlockStateSetting(Level level, BlockPos pos, BlockState originalState, int mode) {
        if (isCopperPistonBlock()) {
            var opt = PistonTest.tryMapState(originalState);
            if (opt.isPresent()) {
                return level.setBlock(pos, opt.get(), mode);
            }
        }

        return level.setBlock(pos, originalState, mode);
    }

    @ModifyArg(
        method = "moveBlocks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/piston/MovingPistonBlock;newMovingBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;ZZ)Lnet/minecraft/world/level/block/entity/BlockEntity;",
            ordinal = 0
        ),
        index = 1
    )
    private BlockState modifyExtraPistonHead(BlockState originalState) {
        if (isCopperPistonBlock()) {
            return PistonTest.copperPistonHeadBlock.get().withPropertiesOf(originalState);
        }
        return originalState;
    }

    @ModifyArg(
        method = "moveBlocks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/piston/MovingPistonBlock;newMovingBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;ZZ)Lnet/minecraft/world/level/block/entity/BlockEntity;",
            ordinal = 1
        ),
        index = 1
    )
    private BlockState modifyExtendingPistonHead1(BlockState originalState) {
        if (isCopperPistonBlock()) {
            return PistonTest.copperPistonHeadBlock.get().withPropertiesOf(originalState);
        }
        return originalState;
    }

    @ModifyArg(
        method = "moveBlocks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/piston/MovingPistonBlock;newMovingBlockEntity(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/core/Direction;ZZ)Lnet/minecraft/world/level/block/entity/BlockEntity;",
            ordinal = 1
        ),
        index = 2
    )
    private BlockState modifyExtendingPistonHead2(BlockState originalState) {
        if (isCopperPistonBlock()) {
            return PistonTest.copperPistonHeadBlock.get().withPropertiesOf(originalState);
        }
        return originalState;
    }

    /**
     * 336: Update Blocks.PISTON_HEAD to COPPER_PISTON_HEAD
     */
    @Redirect(
        method = "moveBlocks",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;updateNeighborsAt(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;)V",
            ordinal = 2
        )
    )
    private void redirectUpdateNeighbours(Level level, BlockPos pos, Block block) {
        if (isCopperPistonBlock()) {
            var newBlock = PistonTest.copperPistonHeadBlock.get();
            level.updateNeighborsAt(pos, newBlock);
        }

        level.updateNeighborsAt(pos, block);
    }

    @Unique
    private boolean isCopperPistonBlock() {
        var pistonBlockState = ((PistonBaseBlock)(Object)this).defaultBlockState();
        return pistonBlockState.is(PistonTest.copperPistonBlock.get());
    }
}
