package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import svenhjol.charm.entity.GlowPearlEntity;
import svenhjol.charm.event.ClientEntitySpawnCallback;
import svenhjol.charm.module.GlowPearls;
import svenhjol.charm.base.CharmModule;

public class GlowPearlsClient {
    public GlowPearlsClient(CharmModule module) {
        EntityRendererRegistry.INSTANCE.register(GlowPearls.ENTITY, ((dispatcher, context)
            -> new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer())));

        ClientEntitySpawnCallback.EVENT.register(((world, packet, x, y, z, entityType) -> {
            if (entityType == GlowPearls.ENTITY) {
                GlowPearlEntity entity = new GlowPearlEntity(world, x, y, z);
                ClientEntitySpawnCallback.addEntity(world, entity, packet, x, y, z);
            }
        }));
    }
}
