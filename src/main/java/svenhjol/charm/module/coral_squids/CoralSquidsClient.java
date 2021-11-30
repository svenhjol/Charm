package svenhjol.charm.module.coral_squids;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.geom.ModelLayerLocation;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.ClientRegistry;

@ClientModule(module = CoralSquids.class)
public class CoralSquidsClient extends CharmModule {
    public static ModelLayerLocation LAYER;

    @Override
    public void register() {
        LAYER = ClientRegistry.entityModelLayer(CoralSquids.ID, CoralSquidEntityModel.getTexturedModelData().bakeRoot());
        EntityRendererRegistry.register(CoralSquids.CORAL_SQUID, CoralSquidEntityRenderer::new);
    }
}
