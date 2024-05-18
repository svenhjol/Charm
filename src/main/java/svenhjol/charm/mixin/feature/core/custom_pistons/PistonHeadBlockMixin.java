package svenhjol.charm.mixin.feature.core.custom_pistons;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.core.custom_pistons.CustomPistons;
import svenhjol.charm.foundation.Resolve;

@Mixin(PistonHeadBlock.class)
public class PistonHeadBlockMixin {
    @Redirect(
        method = {"isFittingBase", "canSurvive"},
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private boolean redirectBlockStateChecks(BlockState state, Block block) {
        return Resolve.feature(CustomPistons.class).handlers.alsoCheckTags(state, block);
    }
}
