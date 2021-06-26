package svenhjol.charm.mixin.open_double_doors;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
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
import svenhjol.charm.module.open_double_doors.OpenDoubleDoors;

@Mixin(DoorBlock.class)
public class ReactOnDoorOpenedMixin {
    @Inject(
        method = "use",
        at = @At("RETURN")
    )
    private void hookUse(BlockState state, Level level, BlockPos pos, Player player, InteractionHand interactionHand, BlockHitResult hit, CallbackInfoReturnable<InteractionResult> cir) {
        if (cir.getReturnValue() == InteractionResult.CONSUME || cir.getReturnValue() == InteractionResult.SUCCESS)
            OpenDoubleDoors.tryOpenNeighbour(level, state, pos, state.getValue(DoorBlock.OPEN));
    }

    @Inject(
        method = "setOpen",
        at = @At("RETURN")
    )
    private void hookSetOpen(Entity entity, Level level, BlockState state, BlockPos pos, boolean bl, CallbackInfo ci) {
        if (entity != null)
            OpenDoubleDoors.tryOpenNeighbour(level, state, pos, bl);
    }

    @Inject(
        method = "neighborChanged",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/Level;setBlock(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;I)Z"
        )
    )
    private void hookNeighborChanged(BlockState state, Level level, BlockPos pos, Block block, BlockPos pos2, boolean bl, CallbackInfo ci) {
        OpenDoubleDoors.tryOpenNeighbour(level, state, pos, !state.getValue(DoorBlock.OPEN));
    }
}
