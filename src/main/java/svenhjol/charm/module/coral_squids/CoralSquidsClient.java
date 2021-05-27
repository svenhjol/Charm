package svenhjol.charm.module.coral_squids;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.EntityHelper;
import svenhjol.charm.module.CharmClientModule;

public class CoralSquidsClient extends CharmClientModule {
    public static EntityModelLayer LAYER;

    public CoralSquidsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        LAYER = EntityHelper.registerEntityModelLayer(CoralSquids.ID, CoralSquidEntityModel.getTexturedModelData().createModel());
        EntityRendererRegistry.INSTANCE.register(CoralSquids.CORAL_SQUID, CoralSquidEntityRenderer::new);
    }
}
