package svenhjol.charm.mixin.feature.core.custom_wood;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import svenhjol.charm.feature.core.custom_wood.common.Tags;

@Mixin(LivingEntity.class)
public class LivingEntityMixin {

    @ModifyExpressionValue(
            method = "trapdoorUsableAsLadder",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/world/level/block/Block;)Z")
    )
    private boolean hookTrapdoorUsableAsLadder(boolean original, @Local(ordinal = 1) BlockState state) {
        return state.is(Tags.FABRIC_LADDERS);
    }
}
