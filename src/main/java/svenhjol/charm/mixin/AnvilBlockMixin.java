package svenhjol.charm.mixin;

import net.minecraft.block.AnvilBlock;
import net.minecraft.block.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.module.AnvilImprovements;

@Mixin(AnvilBlock.class)
public class AnvilBlockMixin {
    @Inject(
        method = "getLandingState",
        at = @At("HEAD"),
        cancellable = true
    )
    private static void hookDamage(BlockState state, CallbackInfoReturnable<BlockState> cir) {
        if (AnvilImprovements.tryDamageAnvil())
            cir.setReturnValue(state);
    }
}
