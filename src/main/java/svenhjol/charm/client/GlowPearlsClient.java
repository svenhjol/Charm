package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import svenhjol.charm.module.GlowPearls;
import svenhjol.meson.MesonModule;

public class GlowPearlsClient {
    public GlowPearlsClient(MesonModule module) {
        EntityRendererRegistry.INSTANCE.register(GlowPearls.ENTITY, ((dispatcher, context)
            -> new FlyingItemEntityRenderer(dispatcher, context.getItemRenderer())));
    }
}
