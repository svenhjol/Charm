package svenhjol.charm.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import svenhjol.charm.event.ClientJoinCallback;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {
    @Inject(
        method = "joinWorld",
        at = @At("RETURN")
    )
    private void hookJoinWorld(ClientWorld world, CallbackInfo ci) {
        ClientJoinCallback.EVENT.invoker().interact((MinecraftClient)(Object)this);
    }
}

