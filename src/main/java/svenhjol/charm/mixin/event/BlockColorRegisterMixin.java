package svenhjol.charm.mixin.event;

import net.minecraft.client.color.block.BlockColors;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.api.event.BlockColorRegisterCallback;

@Mixin(BlockColors.class)
public class BlockColorRegisterMixin {
    @Inject(
        method = "createDefault",
        at = @At("RETURN")
    )
    private static void hookCreateDefault(CallbackInfoReturnable<BlockColors> cir) {
        BlockColorRegisterCallback.EVENT.invoker().interact(cir.getReturnValue());
    }
}
