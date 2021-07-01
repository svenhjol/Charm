package svenhjol.charm.module.endermite_powder;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import svenhjol.charm.loader.ClientModule;

@svenhjol.charm.annotation.ClientModule(module = EndermitePowder.class)
public class EndermitePowderClient extends ClientModule {
    @Override
    public void register() {
        EntityRendererRegistry.INSTANCE.register(EndermitePowder.ENTITY, EndermitePowderEntityRenderer::new);
    }
}
