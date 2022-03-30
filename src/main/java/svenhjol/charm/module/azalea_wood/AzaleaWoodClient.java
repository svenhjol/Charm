package svenhjol.charm.module.azalea_wood;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.model.BoatModel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.ClientRegistry;

@ClientModule(module = AzaleaWood.class)
public class AzaleaWoodClient extends CharmModule {

    @Override
    public void register() {
        // cut-out the transparent areas of the blocks
        BlockRenderLayerMap.INSTANCE.putBlock(AzaleaWood.DOOR, RenderType.cutout());
        BlockRenderLayerMap.INSTANCE.putBlock(AzaleaWood.TRAPDOOR, RenderType.cutout());

        // register boat model
        ClientRegistry.entityModelLayer(new ResourceLocation(Charm.MOD_ID, "boat/azalea"), BoatModel.createBodyModel(false).bakeRoot());
        ClientRegistry.entityModelLayer(new ResourceLocation(Charm.MOD_ID, "chest_boat/azalea"), BoatModel.createBodyModel(true).bakeRoot());

        // register sign material
        ClientRegistry.signMaterial(AzaleaWood.SIGN_TYPE);
    }
}
