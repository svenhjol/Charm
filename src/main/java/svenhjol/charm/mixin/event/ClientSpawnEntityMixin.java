package svenhjol.charm.mixin.event;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import svenhjol.charm.event.AddEntityCallback;
import svenhjol.charm.event.ClientSpawnEntityCallback;

@Mixin(ClientPacketListener.class)
public class ClientSpawnEntityMixin {
    @Shadow private ClientLevel level;

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
        method = "handleAddEntity",
        at = @At("RETURN"),
        locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void hookOnEntitySpawn(ClientboundAddEntityPacket packet, CallbackInfo ci, EntityType<?> entityType) {
        ClientSpawnEntityCallback.EVENT.invoker().interact(packet, entityType, level, packet.getX(), packet.getY(), packet.getZ());
    }
}
