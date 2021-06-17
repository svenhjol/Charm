package svenhjol.charm.module.woodcutters;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.helper.ClientHelper;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class WoodcuttersClient extends CharmClientModule {
    public WoodcuttersClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ClientHelper.registerScreenHandler(Woodcutters.SCREEN_HANDLER, WoodcutterScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Woodcutters.WOODCUTTER, RenderType.cutout());
    }
}
