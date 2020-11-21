package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.module.Mooblooms;
import svenhjol.charm.render.MoobloomEntityRenderer;

public class MoobloomsClient extends CharmClientModule {
    public MoobloomsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        EntityRendererRegistry.INSTANCE.register(Mooblooms.MOOBLOOM, (dispatcher, context)
            -> new MoobloomEntityRenderer(dispatcher));
    }
}
