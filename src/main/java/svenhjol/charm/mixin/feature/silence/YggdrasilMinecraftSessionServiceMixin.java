package svenhjol.charm.mixin.feature.silence;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilMinecraftSessionService;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charm.feature.silence.Silence;

@Mixin(YggdrasilMinecraftSessionService.class)
public class YggdrasilMinecraftSessionServiceMixin {
    @Inject(
        method = "fillGameProfile",
        at = @At("HEAD"),
        cancellable = true,
        remap = false
    )
    private void hookFetchProfileUncached(GameProfile profile, boolean requireSecure, CallbackInfoReturnable<GameProfile> cir) {
        if (Silence.disableDevEnvironmentConnections()) {
            cir.setReturnValue(null);
        }
    }
}
