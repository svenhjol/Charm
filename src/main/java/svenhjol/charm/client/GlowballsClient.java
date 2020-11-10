package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.EntityType;
import net.minecraft.network.packet.s2c.play.EntitySpawnS2CPacket;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.entity.GlowballEntity;
import svenhjol.charm.event.ClientEntitySpawnCallback;
import svenhjol.charm.module.Glowballs;

public class GlowballsClient {
    public GlowballsClient(CharmModule module) {
        EntityRendererRegistry.INSTANCE.register(Glowballs.GLOWBALL, ((dispatcher, context)
            -> new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer())));

        ClientEntitySpawnCallback.EVENT.register(this::handleClientEntitySpawn);
    }

    private void handleClientEntitySpawn(ClientWorld world, EntitySpawnS2CPacket packet, double x, double y, double z, EntityType<?> entityType) {
        if (entityType == Glowballs.GLOWBALL) {
            GlowballEntity entity = new GlowballEntity(world, x, y, z);
            ClientEntitySpawnCallback.addEntity(world, entity, packet, x, y, z);
        }
    }
}
