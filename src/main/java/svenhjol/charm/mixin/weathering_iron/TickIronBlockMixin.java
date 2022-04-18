package svenhjol.charm.mixin.weathering_iron;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.weathering_iron.WeatheringIron;

import java.util.Random;

/**
 * Add random tick to the vanilla iron block so that it can weather.
 */
@Mixin(Block.class)
public abstract class TickIronBlockMixin extends BlockBehaviour {
    public TickIronBlockMixin(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, Random random) {
        super.randomTick(state, level, pos, random);
        if (state == Blocks.IRON_BLOCK.defaultBlockState()) {
            WeatheringIron.handleRandomTick(level, pos, state, random);
        }
    }

    @Inject(
        method = "isRandomlyTicking",
        at = @At("HEAD"),
        cancellable = true
    )
    private void hookIsRandomlyTicking(BlockState blockState, CallbackInfoReturnable<Boolean> cir) {
        if (blockState == Blocks.IRON_BLOCK.defaultBlockState()) {
            cir.setReturnValue(true);
        }
    }
}
