package svenhjol.charm.mixin.piston_test;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.piston_test.PistonTest;

@Mixin(PistonMovingBlockEntity.class)
public abstract class PistonMovingBlockEntityMixin extends BlockEntity {
    public PistonMovingBlockEntityMixin(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
        super(blockEntityType, blockPos, blockState);
    }

    @Inject(
        method = "getCollisionRelatedBlockState",
        at = @At("RETURN"),
        cancellable = true
    )
    private void hookGetCollisionRelatedBlockState(CallbackInfoReturnable<BlockState> cir) {
        if (this.getBlockState().is(PistonTest.movingCopperPistonBlock.get())) {
            var opt = PistonTest.tryMapState(cir.getReturnValue());
            opt.ifPresent(cir::setReturnValue);
        }
    }
}
