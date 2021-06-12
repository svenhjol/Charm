package svenhjol.charm.mixin.callback;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.LoadServerFinishCallback;

@Mixin(MinecraftServer.class)
public class LoadServerFinishCallbackMixin {
    /**
     * Fires the {@link LoadServerFinishCallback} event.
     *
     * This is used by the Charm loader to initialize decorations
     * and custom advancements.  It can be used by any Charm module
     * to perform its own init when a server world has finished loading.
     */
    @Inject(
        method = "createLevels",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/server/level/ServerLevel;getDataStorage()Lnet/minecraft/world/level/storage/DimensionDataStorage;"
        )
    )
    private void hookCreateWorlds(CallbackInfo ci) {
        LoadServerFinishCallback.EVENT.invoker().interact((MinecraftServer)(Object)this);
    }
}
