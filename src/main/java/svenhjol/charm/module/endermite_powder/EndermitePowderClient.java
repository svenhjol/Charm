package svenhjol.charm.module.endermite_powder;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class EndermitePowderClient extends CharmClientModule {
    public EndermitePowderClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        EntityRendererRegistry.INSTANCE.register(EndermitePowder.ENTITY, EndermitePowderEntityRenderer::new);
    }
}
