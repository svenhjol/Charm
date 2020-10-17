package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import svenhjol.charm.module.core.GlowPearls;
import svenhjol.charm.base.CharmModule;

public class GlowPearlsClient {
    public GlowPearlsClient(CharmModule module) {
        EntityRendererRegistry.INSTANCE.register(GlowPearls.ENTITY, ((dispatcher, context)
            -> new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer())));
    }
}
