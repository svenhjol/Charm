package svenhjol.charm.mixin.auto_restock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.module.auto_restock.AutoRestock;

@Mixin(ComposterBlock.class)
public class AddStatWhenCompostedMixin {

    /**
     * Allows autorestock of a composted item.
     */
    @Inject(
        method = "onUse",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void hookOnUse(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit,
        CallbackInfoReturnable<InteractionResult> cir, int i, ItemStack itemstack) {
        AutoRestock.addItemUsedStat(player, itemstack);
    }
}
