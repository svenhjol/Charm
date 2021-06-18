package svenhjol.charm.mixin.core;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.EnchantmentTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.annotation.CharmMixin;
import svenhjol.charm.init.CharmTags;

import java.util.Random;

/**
 * This mixin is one of the most likely to conflict as other modders
 * will target this to implement enchanting power to their own modded blocks.
 *
 * I have created a tag called c:provides_enchanting_power that contains
 * the blocks that Charm uses for enchanting.  If you want to disable this mixin
 * due to conflict, please add the above tag to your mod for Charm to function properly.
 *
 * See {@link CheckEnchantingPowerMixin} for the server-side implementation of this.
 */
@Mixin(EnchantmentTableBlock.class)
@CharmMixin(disableIfModsPresent = {"betterend"})
public abstract class ShowEnchantmentParticlesMixin extends BaseEntityBlock {
    protected ShowEnchantmentParticlesMixin(Properties properties) {
        super(properties);
    }

    @Inject(
        method = "animateTick",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookAnimateTick(BlockState blockState, Level level, BlockPos blockPos, Random random, CallbackInfo ci) {
        super.animateTick(blockState, level, blockPos, random);

        for(int i = -2; i <= 2; ++i) {
            for(int j = -2; j <= 2; ++j) {
                if (i > -2 && i < 2 && j == -1) {
                    j = 2;
                }

                if (random.nextInt(16) == 0) {
                    for(int k = 0; k <= 1; ++k) {
                        BlockPos blockPos2 = blockPos.offset(i, k, j);
                        if (level.getBlockState(blockPos2).is(CharmTags.PROVIDE_ENCHANTING_POWER)) {
                            if (!level.isEmptyBlock(blockPos.offset(i / 2, 0, j / 2))) {
                                break;
                            }

                            level.addParticle(ParticleTypes.ENCHANT, (double)blockPos.getX() + 0.5D, (double)blockPos.getY() + 2.0D, (double)blockPos.getZ() + 0.5D, (double)((float)i + random.nextFloat()) - 0.5D, (double)((float)k - random.nextFloat() - 1.0F), (double)((float)j + random.nextFloat()) - 0.5D);
                        }
                    }
                }
            }
        }
    }
}
