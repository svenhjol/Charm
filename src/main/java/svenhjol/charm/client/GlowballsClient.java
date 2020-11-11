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

@SuppressWarnings({"rawtypes", "unchecked"})
public class GlowballsClient {
    public GlowballsClient(CharmModule module) {
        EntityRendererRegistry.INSTANCE.register(Glowballs.GLOWBALL, ((dispatcher, context)
            -> new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer())));

        ClientEntitySpawnCallback.EVENT.register(this::handleClientEntitySpawn);
    }

    private void handleClientEntitySpawn(EntitySpawnS2CPacket packet, EntityType<?> entityType, ClientWorld world, double x, double y, double z) {
        if (entityType == Glowballs.GLOWBALL) {
            ClientEntitySpawnCallback.addEntity(packet, world, new GlowballEntity(world, x, y, z));
        }
    }
}
