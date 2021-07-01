package svenhjol.charm.module.coral_squids;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.loader.ClientModule;

@svenhjol.charm.annotation.ClientModule(module = CoralSquids.class)
public class CoralSquidsClient extends ClientModule {
    public static ModelLayerLocation LAYER;

    @Override
    public void register() {
        LAYER = ClientHelper.registerEntityModelLayer(CoralSquids.ID, CoralSquidEntityModel.getTexturedModelData().bakeRoot());
        EntityRendererRegistry.INSTANCE.register(CoralSquids.CORAL_SQUID, CoralSquidEntityRenderer::new);
    }
}
