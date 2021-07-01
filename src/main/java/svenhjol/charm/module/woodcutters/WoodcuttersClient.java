package svenhjol.charm.module.woodcutters;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.loader.CharmClientModule;

@ClientModule(module = Woodcutters.class)
public class WoodcuttersClient extends CharmClientModule {

    @Override
    public void register() {
        ClientHelper.registerScreenHandler(Woodcutters.SCREEN_HANDLER, WoodcutterScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Woodcutters.WOODCUTTER, RenderType.cutout());
    }
}
