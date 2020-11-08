package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.module.EndermitePowder;
import svenhjol.charm.render.EndermitePowderEntityRenderer;

public class EndermitePowderClient {
    public EndermitePowderClient(CharmModule module) {
        EntityRendererRegistry.INSTANCE.register(EndermitePowder.ENTITY, EndermitePowderEntityRenderer::new);
    }
}
