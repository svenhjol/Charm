package svenhjol.charm.mixin.callback;

import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.event.ClientSpawnEntityCallback;

@Mixin(ClientPlayNetworkHandler.class)
public class ClientSpawnEntityCallbackMixin {
    @Shadow private ClientWorld world;

    /**
     * Fires the {@link ClientSpawnEntityCallback} event.
     * 
     * This is used by Charm modules to render entities on the client after
     * they spawn in the game, for example, custom projectiles.
     *
     * It is lower-level than {@link AddEntityCallback}.
     * Use that event if you just need to know about the entity that was added
     * rather than the network packet.
     */
    @Inject(
        method = "onEntitySpawn",
        at = @At("RETURN"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookOnEntitySpawn(EntitySpawnS2CPacket packet, CallbackInfo ci, EntityType<?> entityType) {
        ClientSpawnEntityCallback.EVENT.invoker().interact(packet, entityType, world, packet.getX(), packet.getY(), packet.getZ());
    }
}
