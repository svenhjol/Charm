package svenhjol.meson.event;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;

public interface ClientEntitySpawnCallback {
    Event<ClientEntitySpawnCallback> EVENT = EventFactory.createArrayBacked(ClientEntitySpawnCallback.class, (listeners) -> (world, packet, x, y, z, entityType) -> {
        for (ClientEntitySpawnCallback listener : listeners) {
            listener.interact(world, packet, x, y, z, entityType);
        }
    });

    void interact(ClientWorld world, EntitySpawnS2CPacket packet, double x, double y, double z, EntityType<?> entityType);

    static void addEntity(ClientWorld world, Entity entity, EntitySpawnS2CPacket packet, double x, double y, double z) {
        int i = packet.getId();
        entity.updateTrackedPosition(x, y, z);
        entity.refreshPositionAfterTeleport(x, y, z);
        entity.pitch = (float)(packet.getPitch() * 360) / 256.0F;
        entity.yaw = (float)(packet.getYaw() * 360) / 256.0F;
        entity.setEntityId(i);
        entity.setUuid(packet.getUuid());
        world.addEntity(i, entity);
    }
}
