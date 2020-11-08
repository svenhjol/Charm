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
    Event<ClientEntitySpawnCallback> EVENT = EventFactory.createArrayBacked(ClientEntitySpawnCallback.class, (listeners) -> (world, packet, entityType, entity) -> {
        for (ClientEntitySpawnCallback listener : listeners) {
            listener.interact(world, packet, entityType, entity);
        }
    });

    void interact(ClientWorld world, EntitySpawnS2CPacket packet, EntityType<?> entityType, Entity entity);

    static void addEntity(ClientWorld world, Entity entity, EntitySpawnS2CPacket packet) {
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
