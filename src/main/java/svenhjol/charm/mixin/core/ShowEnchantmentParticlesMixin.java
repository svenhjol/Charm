package svenhjol.charm.mixin.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.helper.EnchantmentsHelper;

import java.util.Random;

@Mixin(EnchantmentTableBlock.class)
public class ShowEnchantmentParticlesMixin {

    /**
     * Adds the enchantment particle if the block provides enchanting power.
     */
    @Inject(
        method = "animateTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/core/BlockPos;offset(III)Lnet/minecraft/core/BlockPos;",
            shift = At.Shift.AFTER,
            ordinal = 0
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookRandomDisplayTick(BlockState stateIn, Level world, BlockPos pos, Random random, CallbackInfo ci,
        int i, int j, int k) {
        BlockState state = world.getBlockState(pos.offset(i, k, j));
        if (EnchantmentsHelper.canBlockPowerEnchantingTable(state))
            // copypasta from EnchantingTableBlock:61
            world.addParticle(ParticleTypes.ENCHANT, (double)pos.getX() + 0.5D, (double)pos.getY() + 2.0D, (double)pos.getZ() + 0.5D, (double)((float)i + random.nextFloat()) - 0.5D, (double)((float)k - random.nextFloat() - 1.0F), (double)((float)j + random.nextFloat()) - 0.5D);
    }
}
