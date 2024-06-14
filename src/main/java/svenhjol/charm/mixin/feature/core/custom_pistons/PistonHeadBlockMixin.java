package svenhjol.charm.mixin.feature.core.custom_pistons;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.core.custom_pistons.CustomPistons;

@Mixin(PistonHeadBlock.class)
public class PistonHeadBlockMixin {

    @WrapOperation(
            method = {"isFittingBase", "canSurvive"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
            )
    )
    private boolean redirectBlockStateChecks(BlockState instance, Block block, Operation<Boolean> original) {
        if (Resolve.feature(CustomPistons.class).handlers.alsoCheckTags(instance, block)) {
            return true;
        }
        return original.call(instance, block);
    }
}
