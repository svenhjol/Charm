package svenhjol.charm.mixin.feature.core.custom_pistons;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.charmony.Resolve;
import svenhjol.charm.feature.core.custom_pistons.CustomPistons;

@Mixin(PistonBaseBlock.class)
public class PistonBaseBlockMixin {
    @Redirect(
        method = {"checkIfExtend", "triggerEvent", "moveBlocks"},
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private boolean redirectBlockStateChecks(BlockState state, Block block) {
        return Resolve.feature(CustomPistons.class).handlers.alsoCheckTags(state, block);
    }

    @Redirect(
        method = {"isPushable"},
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private static boolean redirectStaticBlockStateChecks(BlockState state, Block block) {
        return Resolve.feature(CustomPistons.class).handlers.alsoCheckTags(state, block);
    }
}
