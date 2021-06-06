package svenhjol.charm.mixin.anvil_improvements;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.anvil_improvements.AnvilImprovements;

@Mixin(AnvilBlock.class)
public class ReduceAnvilDamageMixin {

    /**
     * When the anvil takes any damage, defer to tryDamageAnvil.
     * If stronger anvils config is enabled and a random check passes,
     * return early from this method.
     */
    @Inject(
        method = "damage",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookDamage(BlockState state, CallbackInfoReturnable<BlockState> cir) {
        if (AnvilImprovements.tryDamageAnvil())
            cir.setReturnValue(state);
    }
}
