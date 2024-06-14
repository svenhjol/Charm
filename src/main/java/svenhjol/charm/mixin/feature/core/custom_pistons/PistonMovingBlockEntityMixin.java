package svenhjol.charm.mixin.feature.core.custom_pistons;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.core.custom_pistons.CustomPistons;

@Mixin(PistonMovingBlockEntity.class)
public class PistonMovingBlockEntityMixin {

    @WrapOperation(
            method = {"finalTick"},
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

    @WrapOperation(
            method = {"tick"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
            )
    )
    private static boolean redirectStaticBlockStateChecks(BlockState instance, Block block, Operation<Boolean> original) {
        if (Resolve.feature(CustomPistons.class).handlers.alsoCheckTags(instance, block)) {
            return true;
        }
        return original.call(instance, block);
    }
}
