package svenhjol.charm.mixin.feature.silence_microsoft;

import com.mojang.authlib.minecraft.client.MinecraftClient;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.authlib.yggdrasil.YggdrasilServicesKeyInfo;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.silence_microsoft.CommonCallbacks;

import java.net.URL;
import java.util.Optional;

@Mixin(YggdrasilServicesKeyInfo.class)
public class YggdrasilServicesKeyInfoMixin {
    @Inject(
        method = "fetch",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private static void hookFetch(URL url, MinecraftClient client, CallbackInfoReturnable<Optional<ServicesKeySet>> cir) {
        if (CommonCallbacks.disableDevEnvironmentConnections()) {
            cir.setReturnValue(Optional.empty());
        }
    }
}
