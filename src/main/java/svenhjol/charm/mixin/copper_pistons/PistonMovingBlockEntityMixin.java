package svenhjol.charm.mixin.copper_pistons;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.copper_pistons.CopperPistons;

@SuppressWarnings("UnnecessaryLocalVariable")
@Mixin(PistonMovingBlockEntity.class)
public abstract class PistonMovingBlockEntityMixin extends BlockEntity {
    public PistonMovingBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Redirect(
        method = "getCollisionRelatedBlockState",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"
        )
    )
    private BlockState modifyPistonHead(Block originalInstance) {
        Block newInstance = null;

        if (isCopperPistonBlock()) {
            newInstance = CopperPistons.copperPistonHeadBlock.get();
        }

        var state = (newInstance != null ? newInstance : originalInstance).defaultBlockState();
        return state;
    }

    @Unique
    private boolean isCopperPistonBlock() {
        var blockState = getBlockState();
        return blockState.is(CopperPistons.movingCopperPistonBlock.get());
    }
}
