package svenhjol.charm.mixin.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.init.CharmTags;

@Mixin(EnchantmentTableBlock.class)
public class CheckEnchantingPowerMixin {
    @Inject(
        method = "isValidBookShelf",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookIsValidBookShelf(Level level, BlockPos blockPos, BlockPos blockPos2, CallbackInfoReturnable<Boolean> cir) {
        if (level.getBlockState(blockPos.offset(blockPos2)).is(CharmTags.PROVIDES_ENCHANTING_POWER) && level.isEmptyBlock(blockPos.offset(blockPos2.getX() / 2, blockPos2.getY(), blockPos2.getZ() / 2))) {
            cir.setReturnValue(true);
        }
    }
}