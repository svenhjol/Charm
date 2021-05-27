package svenhjol.charm.module.mooblooms;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.EntityHelper;

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
