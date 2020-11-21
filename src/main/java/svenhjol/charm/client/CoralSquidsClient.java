package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.module.CoralSquids;
import svenhjol.charm.render.CoralSquidEntityRenderer;
import svenhjol.charm.base.CharmModule;

public class CoralSquidsClient extends CharmClientModule {
    public CoralSquidsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        EntityRendererRegistry.INSTANCE.register(CoralSquids.CORAL_SQUID, (dispatcher, context)
            -> new CoralSquidEntityRenderer(dispatcher));
    }
}
