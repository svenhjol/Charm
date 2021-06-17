package svenhjol.charm.module.extra_boats;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class ExtraBoatsClient extends CharmClientModule {
    public ExtraBoatsClient(CharmModule module) {
        super(module);
    }

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
