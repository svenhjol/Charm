package svenhjol.charm.mixin.stronger_anvils;

import net.minecraft.world.level.block.AnvilBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.stonger_anvils.StrongerAnvils;

@Mixin(AnvilBlock.class)
public class ReduceAnvilDamageMixin {
    /**
     * When the anvil takes any damage, defer to tryDamageAnvil.
     * If a random check passes then return early so that the anvil does not get damaged.
     */
    @Inject(
        method = "damage",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookDamage(BlockState state, CallbackInfoReturnable<BlockState> cir) {
        if (StrongerAnvils.tryDamageAnvil()) {
            cir.setReturnValue(state);
        }
    }
}
