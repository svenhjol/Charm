package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;

/**
 * Lets entities be created on the client when spawned on the server.
 */
public interface ClientSpawnEntityEvent {
    Event<ClientSpawnEntityEvent> EVENT = EventFactory.createArrayBacked(ClientSpawnEntityEvent.class, (listeners) -> (packet, entityType, world, x, y, z) -> {
        for (ClientSpawnEntityEvent listener : listeners) {
            listener.interact(packet, entityType, world, x, y, z);
        }
    });

    void interact(ClientboundAddEntityPacket packet, EntityType<?> entityType, ClientLevel world, double x, double y, double z);

    static void addEntity(ClientboundAddEntityPacket packet, ClientLevel world, Entity entity) {
        int id = packet.getId();

        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();

        entity.setPacketCoordinates(x, y, z);
        entity.moveTo(x, y, z);
        entity.setXRot((float)(packet.getxRot() * 360) / 256.0F);
        entity.setYRot((float)(packet.getyRot() * 360) / 256.0F);
        entity.setId(id);
        entity.setUUID(packet.getUUID());
        world.putNonPlayerEntity(id, entity);
    }
}
