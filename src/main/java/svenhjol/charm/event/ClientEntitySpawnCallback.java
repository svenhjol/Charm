package svenhjol.charm.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

/**
 * Lets entities be created on the client when spawned on the server.
 */
public interface ClientEntitySpawnCallback {
    Event<ClientEntitySpawnCallback> EVENT = EventFactory.createArrayBacked(ClientEntitySpawnCallback.class, (listeners) -> (packet, entityType, world, x, y, z) -> {
        for (ClientEntitySpawnCallback listener : listeners) {
            listener.interact(packet, entityType, world, x, y, z);
        }
    });

    void interact(EntitySpawnS2CPacket packet, EntityType<?> entityType, ClientWorld world, double x, double y, double z);

    static void addEntity(EntitySpawnS2CPacket packet, ClientWorld world, Entity entity) {
        int id = packet.getId();

        double x = packet.getX();
        double y = packet.getY();
        double z = packet.getZ();

        entity.updateTrackedPosition(x, y, z);
        entity.refreshPositionAfterTeleport(x, y, z);
        entity.pitch = (float)(packet.getPitch() * 360) / 256.0F;
        entity.yaw = (float)(packet.getYaw() * 360) / 256.0F;
        entity.setEntityId(id);
        entity.setUuid(packet.getUuid());
        world.addEntity(id, entity);
    }
}
