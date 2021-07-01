package svenhjol.charm.module.extra_boats;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.loader.CharmClientModule;

@ClientModule(module = ExtraBoats.class)
public class ExtraBoatsClient extends CharmClientModule {
    @Override
    public void register() {
        // nether-wood boats
        for (String s : new String[]{"crimson", "warped"}) {
            ClientHelper.registerEntityModelLayer(new ResourceLocation(Charm.MOD_ID, "boat/" + s), BoatModel.createBodyModel().bakeRoot());
        }

        // register charm boats renderer
        EntityRendererRegistry.INSTANCE.register(ExtraBoats.CHARM_BOAT, CharmBoatEntityRenderer::new);
    }
}
