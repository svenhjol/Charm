package svenhjol.charm.mixin.feature.anvils_last_longer;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.anvils_last_longer.AnvilsLastLonger;
import svenhjol.charm.foundation.Resolve;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
    /**
     * When the anvil takes any damage, defer to {@link svenhjol.charm.feature.anvils_last_longer.common.Handlers#tryDamageAnvil}.
     * If a random check passes then return early so that the anvil does not get damaged.
     */
    @Inject(
        method = "damage",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookDamage(BlockState state, CallbackInfoReturnable<BlockState> cir) {
        if (Resolve.feature(AnvilsLastLonger.class).handlers.tryDamageAnvil()) {
            cir.setReturnValue(state);
        }
    }
}
