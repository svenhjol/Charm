package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.event.ClientEntitySpawnCallback;
import svenhjol.charm.module.GlowBalls;

public class GlowBallsClient {
    public GlowBallsClient(CharmModule module) {
        EntityRendererRegistry.INSTANCE.register(GlowBalls.ENTITY, dispatcher
            -> new FlyingItemEntityRenderer<>(dispatcher, 1.0F, true));

        ClientEntitySpawnCallback.EVENT.register(this::handleClientEntitySpawn);
    }

    private void handleClientEntitySpawn(ClientWorld world, EntitySpawnS2CPacket packet, EntityType<?> entityType, Entity entity) {
        if (entityType == GlowBalls.ENTITY) {
            ClientEntitySpawnCallback.addEntity(world, entity, packet);
        }
    }
}
