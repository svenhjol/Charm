package svenhjol.charm.mixin.weathering_iron;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.CarvedPumpkinBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import svenhjol.charm.module.weathering_iron.WeatheringIron;

import java.util.function.Predicate;

@Mixin(CarvedPumpkinBlock.class)
public class WaxedIronGolemBuildingMixin {
    private static final Predicate<BlockState> IRON_BLOCKS_PREDICATE =
        state -> state != null
        && (state.is(Blocks.IRON_BLOCK) || state.is(WeatheringIron.WAXED_IRON));

    @ModifyArg(
        method = "getOrCreateIronGolemBase",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/pattern/BlockInWorld;hasState(Ljava/util/function/Predicate;)Ljava/util/function/Predicate;",
            ordinal = 0
        ),
        index = 0
    )
    private Predicate<BlockState> hookBaseCheck(Predicate<BlockState> predicate) {
        return IRON_BLOCKS_PREDICATE;
    }

    @ModifyArg(
        method = "getOrCreateIronGolemFull",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/pattern/BlockInWorld;hasState(Ljava/util/function/Predicate;)Ljava/util/function/Predicate;",
            ordinal = 1
        ),
        index = 0
    )
    private Predicate<BlockState> hookFullCheck(Predicate<BlockState> predicate) {
        return IRON_BLOCKS_PREDICATE;
    }
}
