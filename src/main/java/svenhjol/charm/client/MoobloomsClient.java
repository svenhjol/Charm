package svenhjol.charm.client;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.EntityHelper;
import svenhjol.charm.module.Mooblooms;
import svenhjol.charm.render.MoobloomEntityRenderer;

public class MoobloomsClient extends CharmClientModule {
    public static EntityModelLayer LAYER;

    public MoobloomsClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        LAYER = EntityHelper.registerEntityModelLayer(Mooblooms.ID, CowEntityModel.getTexturedModelData().createModel());
        EntityRendererRegistry.INSTANCE.register(Mooblooms.MOOBLOOM, MoobloomEntityRenderer::new);
    }
}
