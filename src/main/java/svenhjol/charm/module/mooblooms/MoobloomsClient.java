package svenhjol.charm.module.mooblooms;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.helper.ClientRegistryHelper;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = Mooblooms.class)
public class MoobloomsClient extends CharmModule {
    public static ModelLayerLocation LAYER;

    @Override
    public void register() {
        LAYER = ClientRegistryHelper.entityModelLayer(Mooblooms.ID, CowModel.createBodyLayer().bakeRoot());
        EntityRendererRegistry.INSTANCE.register(Mooblooms.MOOBLOOM, MoobloomEntityRenderer::new);
    }
}
