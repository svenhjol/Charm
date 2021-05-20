package svenhjol.charm.client;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.model.BoatEntityModel;
import net.minecraft.util.Identifier;
import svenhjol.charm.Charm;
import svenhjol.charm.base.CharmClientModule;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.EntityHelper;
import svenhjol.charm.module.AzaleaWood;

public class AzaleaWoodClient extends CharmClientModule {
    public AzaleaWoodClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        // cut-out the transparent areas of the blocks
        BlockRenderLayerMap.INSTANCE.putBlock(AzaleaWood.DOOR, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AzaleaWood.TRAPDOOR, RenderLayer.getCutout());

        // register boat model
        EntityHelper.registerEntityModelLayer(new Identifier(Charm.MOD_ID, "boat/azalea"), BoatEntityModel.getTexturedModelData().createModel());
    }
}
