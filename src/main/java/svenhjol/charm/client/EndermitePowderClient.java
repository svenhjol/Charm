package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EndermiteEntityRenderer;
import svenhjol.charm.module.core.EndermitePowder;
import svenhjol.charm.base.CharmModule;

public class EndermitePowderClient {
    public EndermitePowderClient(CharmModule module) {
        EntityRendererRegistry.INSTANCE.register(EndermitePowder.ENTITY, ((dispatcher, context)
            -> new EndermiteEntityRenderer(dispatcher)));
    }
}
