package svenhjol.meson.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.resource.ResourcePackManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import svenhjol.meson.event.ClientJoinCallback;

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
        ClientJoinCallback.EVENT.invoker().interact((MinecraftClient)(Object)this);
    }
}

