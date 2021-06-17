package svenhjol.charm.module.coral_squids;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class CoralSquidsClient extends CharmClientModule {
    public static ModelLayerLocation LAYER;

    public CoralSquidsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        LAYER = ClientHelper.registerEntityModelLayer(CoralSquids.ID, CoralSquidEntityModel.getTexturedModelData().bakeRoot());
        EntityRendererRegistry.INSTANCE.register(CoralSquids.CORAL_SQUID, CoralSquidEntityRenderer::new);
    }
}
