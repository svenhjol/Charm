package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.EntityHelper;
import svenhjol.charm.model.CoralSquidEntityModel;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.module.CoralSquids;
import svenhjol.charm.render.CoralSquidEntityRenderer;

public class CoralSquidsClient extends CharmClientModule {
    public static EntityModelLayer LAYER;

    public CoralSquidsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        LAYER = EntityHelper.registerEntityModelLayer(CoralSquids.ID, CoralSquidEntityModel.render().method_32109());
        EntityRendererRegistry.INSTANCE.register(CoralSquids.CORAL_SQUID, CoralSquidEntityRenderer::new);
    }
}
