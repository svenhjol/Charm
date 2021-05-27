package svenhjol.charm.mixin.core;

import net.minecraft.block.BlockState;
import net.minecraft.block.EnchantingTableBlock;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.helper.EnchantmentsHelper;

import java.util.Random;

@Mixin(EnchantingTableBlock.class)
public class ShowEnchantmentParticlesMixin {

    /**
     * Adds the enchantment particle if the block provides enchanting power.
     */
    @Inject(
        method = "randomDisplayTick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/util/math/BlockPos;add(III)Lnet/minecraft/util/math/BlockPos;",
            shift = At.Shift.AFTER,
            ordinal = 0
        ),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookRandomDisplayTick(BlockState stateIn, World world, BlockPos pos, Random random, CallbackInfo ci,
        int i, int j, int k) {
        BlockState state = world.getBlockState(pos.add(i, k, j));
        if (EnchantmentsHelper.canBlockPowerEnchantingTable(state))
            // copypasta from EnchantingTableBlock:61
            world.addParticle(ParticleTypes.ENCHANT, (double)pos.getX() + 0.5D, (double)pos.getY() + 2.0D, (double)pos.getZ() + 0.5D, (double)((float)i + random.nextFloat()) - 0.5D, (double)((float)k - random.nextFloat() - 1.0F), (double)((float)j + random.nextFloat()) - 0.5D);
    }
}
