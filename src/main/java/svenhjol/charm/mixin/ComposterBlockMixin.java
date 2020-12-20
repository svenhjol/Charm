package svenhjol.charm.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ComposterBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.module.AutoRestock;

@Mixin(ComposterBlock.class)
public class ComposterBlockMixin {

    @Inject(
        method = "onUse",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;syncWorldEvent(ILnet/minecraft/util/math/BlockPos;I)V"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    public void hookOnUse(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockHitResult hit,
                                     CallbackInfoReturnable<ActionResult> cir, int i, ItemStack itemstack) {
        AutoRestock.addItemUsedStat(player, itemstack);
    }
}
