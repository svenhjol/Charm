package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EndermiteEntityRenderer;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.module.EndermitePowder;
import svenhjol.charm.base.CharmModule;

public class EndermitePowderClient extends CharmClientModule {
    public EndermitePowderClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        EntityRendererRegistry.INSTANCE.register(EndermitePowder.ENTITY, (dispatcher, context)
            -> new EndermiteEntityRenderer(dispatcher));
    }
}
