package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EndermiteEntityRenderer;
import svenhjol.charm.module.EndermitePowder;
import svenhjol.meson.MesonModule;

public class EndermitePowderClient {
    public EndermitePowderClient(MesonModule module) {
        EntityRendererRegistry.INSTANCE.register(EndermitePowder.ENTITY, ((dispatcher, context)
            -> new EndermiteEntityRenderer(dispatcher)));
    }
}
