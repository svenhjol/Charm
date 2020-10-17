package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import svenhjol.charm.module.vanillachanges.CoralSquids;
import svenhjol.charm.render.CoralSquidEntityRenderer;
import svenhjol.charm.base.CharmModule;

public class CoralSquidsClient {
    public CoralSquidsClient(CharmModule module) {
        EntityRendererRegistry.INSTANCE.register(CoralSquids.CORAL_SQUID, (((dispatcher, context) ->
            new CoralSquidEntityRenderer(dispatcher))));
    }
}
