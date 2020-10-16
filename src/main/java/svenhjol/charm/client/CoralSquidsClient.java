package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import svenhjol.charm.module.CoralSquids;
import svenhjol.charm.render.CoralSquidEntityRenderer;
import svenhjol.meson.MesonModule;

public class CoralSquidsClient {
    public CoralSquidsClient(MesonModule module) {
        EntityRendererRegistry.INSTANCE.register(CoralSquids.CORAL_SQUID, (((dispatcher, context) ->
            new CoralSquidEntityRenderer(dispatcher))));
    }
}
