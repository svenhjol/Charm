package svenhjol.charm.mixin.feature.variant_pistons;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.variant_pistons.CommonCallbacks;

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
        return CommonCallbacks.alsoCheckTags(state, block);
    }
}
