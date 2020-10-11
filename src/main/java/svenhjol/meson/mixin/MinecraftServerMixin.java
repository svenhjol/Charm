package svenhjol.meson.mixin;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.meson.event.LoadWorldCallback;

@Mixin(MinecraftServer.class)
public class MinecraftServerMixin {
//    @Inject(
//        method = "runServer",
//        at = @At(
//            value = "INVOKE_ASSIGN",
//            target = "Lnet/minecraft/util/Util;getMeasuringTimeMs()J",
//            ordinal = 0
//        )
//    )

    @Inject(
        method = "createWorlds",
        at = @At(
            value = "INVOKE_ASSIGN",
            target = "Lnet/minecraft/server/world/ServerWorld;getPersistentStateManager()Lnet/minecraft/world/PersistentStateManager;"
        )
    )
    private void hookRunServer(CallbackInfo ci) {
        LoadWorldCallback.EVENT.invoker().interact((MinecraftServer)(Object)this);
    }
}
