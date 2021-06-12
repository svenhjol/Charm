package svenhjol.charm.mixin.callback;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.LoadClientStartCallback;

@Mixin(Minecraft.class)
public class LoadWorldClientStartCallbackMixin {
    /**
     * Fires the {@link LoadClientStartCallback} event.
     *
     * This is used by the Charm loader to initialize decorations
     * and custom advancements.  It can be used by any Charm module
     * to perform its own init before a client world starts.
     */
    @Inject(
        method = "doLoadLevel",
        at = @At(
            value = "HEAD"
        )
    )
    private void hookCreateWorlds(CallbackInfo ci) {
        LoadClientStartCallback.EVENT.invoker().interact((Minecraft)(Object)this);
    }
}
