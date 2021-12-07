package svenhjol.charm.module.mooblooms;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.ClientRegistry;

@ClientModule(module = Mooblooms.class)
public class MoobloomsClient extends CharmModule {
    public static ModelLayerLocation LAYER;

    @Override
    public void register() {
        LAYER = ClientRegistry.entityModelLayer(Mooblooms.ID, CowModel.createBodyLayer().bakeRoot());
        EntityRendererRegistry.register(Mooblooms.MOOBLOOM, MoobloomEntityRenderer::new);
    }
}
