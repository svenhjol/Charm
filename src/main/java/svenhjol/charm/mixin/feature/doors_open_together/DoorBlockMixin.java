package svenhjol.charm.mixin.feature.doors_open_together;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.doors_open_together.DoorsOpenTogether;
import svenhjol.charm.foundation.Resolve;

@Mixin(DoorBlock.class)
public class DoorBlockMixin {
    @Inject(
        method = "useWithoutItem",
        at = @At("RETURN")
    )
    private void hookUse(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult blockHitResult, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue() == InteractionResult.CONSUME || cir.getReturnValue() == InteractionResult.SUCCESS) {
            Resolve.feature(DoorsOpenTogether.class).handlers.tryOpenNeighbour(level, state, pos, state.getValue(DoorBlock.OPEN));
        }
    }

    @Inject(
        method = "setOpen",
        at = @At("RETURN")
    )
    private void hookSetOpen(Entity entity, Level level, BlockState state, BlockPos pos, boolean bl, CallbackInfo ci) {
        if (entity != null) {
            Resolve.feature(DoorsOpenTogether.class).handlers.tryOpenNeighbour(level, state, pos, bl);
        }
    }

    @Inject(
        method = "neighborChanged",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
        )
    )
    private void hookNeighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean bl, CallbackInfo ci) {
        Resolve.feature(DoorsOpenTogether.class).handlers.tryOpenNeighbour(level, state, pos, !state.getValue(DoorBlock.OPEN));
    }

    @Inject(
        method = "playSound",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookPlaySound(Entity entity, Level level, BlockPos pos, boolean isClosed, CallbackInfo ci) {
        if (Resolve.feature(DoorsOpenTogether.class).handlers.neighbours.contains(pos)) {
            Resolve.feature(DoorsOpenTogether.class).handlers.neighbours.remove(pos);
            ci.cancel();
        }
    }
}
