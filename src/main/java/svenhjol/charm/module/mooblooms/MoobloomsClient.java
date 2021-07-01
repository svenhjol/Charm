package svenhjol.charm.module.mooblooms;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.loader.ClientModule;

@svenhjol.charm.annotation.ClientModule(module = Mooblooms.class)
public class MoobloomsClient extends ClientModule {
    public static ModelLayerLocation LAYER;

    @Override
    public void register() {
        LAYER = ClientHelper.registerEntityModelLayer(Mooblooms.ID, CowModel.createBodyLayer().bakeRoot());
        EntityRendererRegistry.INSTANCE.register(Mooblooms.MOOBLOOM, MoobloomEntityRenderer::new);
    }
}
