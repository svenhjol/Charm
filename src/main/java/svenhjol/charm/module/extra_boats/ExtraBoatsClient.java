package svenhjol.charm.module.extra_boats;

import net.fabricmc.fabric.api.client.rendereregistry.v1.EntityRendererRegistry;
import net.minecraft.client.model.BoatModel;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.helper.ClientRegistryHelper;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = ExtraBoats.class)
public class ExtraBoatsClient extends CharmModule {
    @Override
    public void register() {
        // nether-wood boats
        for (String s : new String[]{"crimson", "warped"}) {
            ClientRegistryHelper.entityModelLayer(new ResourceLocation(Charm.MOD_ID, "boat/" + s), BoatModel.createBodyModel().bakeRoot());
        }

        // register charm boats renderer
        EntityRendererRegistry.INSTANCE.register(ExtraBoats.CHARM_BOAT, CharmBoatEntityRenderer::new);
    }
}
