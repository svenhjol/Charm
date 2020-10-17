package svenhjol.charm.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.LoadWorldCallback;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
    @Inject(
        method = "createWorlds",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/server/world/ServerWorld;getPersistentStateManager()Lnet/minecraft/world/PersistentStateManager;"
        )
    )
    private void hookCreateWorlds(CallbackInfo ci) {
        LoadWorldCallback.EVENT.invoker().interact((MinecraftServer)(Object)this);
    }
}
