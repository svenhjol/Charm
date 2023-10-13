package svenhjol.charm.mixin.variant_pistons;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.variant_pistons.VariantPistons;

@Mixin(PistonMovingBlockEntity.class)
public class PistonMovingBlockEntityMixin {
    @Redirect(
        method = {"finalTick"},
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private boolean redirectBlockStateChecks(BlockState state, Block block) {
        return VariantPistons.alsoCheckTags(state, block);
    }

    @Redirect(
        method = {"tick"},
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z"
        )
    )
    private static boolean redirectStaticBlockStateChecks(BlockState state, Block block) {
        return VariantPistons.alsoCheckTags(state, block);
    }
}
