package svenhjol.meson.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.resource.ResourcePackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.meson.event.ClientJoinCallback;
import svenhjol.meson.event.ClientReloadPacksCallback;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Redirect(
        method = "<init>",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/resource/ResourcePackManager;scanPacks()V"
        )
    )
    private void hookAfterScanPacks(ResourcePackManager resourcePackManager) {
        resourcePackManager.scanPacks();
        ClientReloadPacksCallback.EVENT.invoker().interact((MinecraftClient)(Object)this);
    }

    @Inject(
        method = "joinWorld",
        at = @At("RETURN")
    )
    private void hookJoinWorld(ClientWorld world, CallbackInfo ci) {
        ClientJoinCallback.EVENT.invoker().interact((MinecraftClient)(Object)this);
    }
}

