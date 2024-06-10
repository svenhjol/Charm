package svenhjol.charm.mixin.feature.core.custom_wood;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.charm.feature.core.custom_wood.common.Tags;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {
    @Redirect(
        method = "trapdoorUsableAsLadder",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
    )
    private boolean hookTrapdoorUsableAsLadder(BlockState state, Block block) {
        return state.is(Tags.LADDERS);
    }
}
