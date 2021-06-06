package svenhjol.charm.module.azalea_wood;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.helper.EntityHelper;
import svenhjol.charm.module.azalea_wood.AzaleaWood;

public class AzaleaWoodClient extends CharmClientModule {
    public AzaleaWoodClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        // cut-out the transparent areas of the blocks
        BlockRenderLayerMap.INSTANCE.putBlock(svenhjol.charm.module.azalea_wood.AzaleaWood.DOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AzaleaWood.TRAPDOOR, RenderType.cutout());

        // register boat model
        EntityHelper.registerEntityModelLayer(new ResourceLocation(Charm.MOD_ID, "boat/azalea"), BoatModel.createBodyModel().bakeRoot());
    }
}
