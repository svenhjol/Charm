package svenhjol.charm.mixin.feature.copper_pistons;

import com.llamalad7.mixinextras.injector.ModifyReceiver;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.copper_pistons.CopperPistons;

@Mixin(PistonMovingBlockEntity.class)
public abstract class PistonMovingBlockEntityMixin extends BlockEntity {
    public PistonMovingBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @ModifyReceiver(
            method = "getCollisionRelatedBlockState",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/Block;defaultBlockState()Lnet/minecraft/world/level/block/state/BlockState;"
            )
    )
    private Block modifyPistonHead(Block originalInstance) {
        Block newInstance = null;

        if (isCopperPistonBlock()) {
            newInstance = Resolve.feature(CopperPistons.class).registers.copperPistonHeadBlock.get();
        }

        return newInstance != null ? newInstance : originalInstance;
    }

    @Unique
    private boolean isCopperPistonBlock() {
        var blockState = getBlockState();
        return blockState.is(Resolve.feature(CopperPistons.class).registers.movingCopperPistonBlock.get());
    }
}
