package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.entity.GlowBallEntity;
import svenhjol.charm.event.ClientEntitySpawnCallback;
import svenhjol.charm.module.GlowBalls;

public class GlowBallsClient {
    public GlowBallsClient(CharmModule module) {
        EntityRendererRegistry.INSTANCE.register(GlowBalls.ENTITY, ((dispatcher, context)
            -> new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer())));

        ClientEntitySpawnCallback.EVENT.register(this::handleClientEntitySpawn);
    }

    private void handleClientEntitySpawn(ClientWorld world, EntitySpawnS2CPacket packet, double x, double y, double z, EntityType<?> entityType) {
        if (entityType == GlowBalls.ENTITY) {
            GlowBallEntity entity = new GlowBallEntity(world, x, y, z);
            ClientEntitySpawnCallback.addEntity(world, entity, packet, x, y, z);
        }
    }
}
