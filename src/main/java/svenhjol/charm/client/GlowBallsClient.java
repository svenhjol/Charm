package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import svenhjol.charm.entity.GlowBallEntity;
import svenhjol.charm.event.ClientEntitySpawnCallback;
import svenhjol.charm.module.GlowBalls;
import svenhjol.charm.base.CharmModule;

public class GlowBallsClient {
    public GlowBallsClient(CharmModule module) {
        EntityRendererRegistry.INSTANCE.register(GlowBalls.ENTITY, ((dispatcher, context)
            -> new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer())));

        ClientEntitySpawnCallback.EVENT.register(((world, packet, x, y, z, entityType) -> {
            if (entityType == GlowBalls.ENTITY) {
                GlowBallEntity entity = new GlowBallEntity(world, x, y, z);
                ClientEntitySpawnCallback.addEntity(world, entity, packet, x, y, z);
            }
        }));
    }
}
