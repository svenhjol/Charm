package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.entity.FlyingItemEntityRenderer;
import net.minecraft.client.render.item.ItemRenderer;
import svenhjol.charm.module.GlowPearls;
import svenhjol.meson.MesonModule;

public class GlowPearlsClient {
    public GlowPearlsClient(MesonModule module) {
        ItemRenderer itemRenderer = MinecraftClient.getInstance().getItemRenderer();
        EntityRendererRegistry.INSTANCE.register(GlowPearls.ENTITY, ((dispatcher, context)
            -> new FlyingItemEntityRenderer(dispatcher, itemRenderer)));
    }
}
