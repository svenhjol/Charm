package svenhjol.charm.mixin;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.event.ClientEntitySpawnCallback;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientPlayNetworkHandlerMixin {
    @Shadow private ClientWorld world;

    @Inject(
        method = "onEntitySpawn",
        at = @At("RETURN"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookOnEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci, double x, double y, double z) {
        ClientEntitySpawnCallback.EVENT.invoker().interact(world, packet, x, y, z, packet.getEntityTypeId());
    }
}
