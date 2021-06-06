package svenhjol.charm.module.coral_squids;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.EntityHelper;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.coral_squids.CoralSquidEntityModel;
import svenhjol.charm.module.coral_squids.CoralSquidEntityRenderer;
import svenhjol.charm.module.coral_squids.CoralSquids;

public class CoralSquidsClient extends CharmClientModule {
    public static ModelLayerLocation LAYER;

    public CoralSquidsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        LAYER = EntityHelper.registerEntityModelLayer(svenhjol.charm.module.coral_squids.CoralSquids.ID, CoralSquidEntityModel.getTexturedModelData().bakeRoot());
        EntityRendererRegistry.INSTANCE.register(CoralSquids.CORAL_SQUID, CoralSquidEntityRenderer::new);
    }
}
