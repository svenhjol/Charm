package svenhjol.charm.module.endermite_powder;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = EndermitePowder.class)
public class EndermitePowderClient extends CharmModule {
    @Override
    public void register() {
        EntityRendererRegistry.INSTANCE.register(EndermitePowder.ENTITY, EndermitePowderEntityRenderer::new);
    }
}
