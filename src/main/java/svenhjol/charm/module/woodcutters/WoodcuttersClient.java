package svenhjol.charm.module.woodcutters;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.screenhandler.v1.ScreenRegistry;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.module.CharmClientModule;
import svenhjol.charm.module.CharmModule;

public class WoodcuttersClient extends CharmClientModule {
    public WoodcuttersClient(CharmModule module) {
        super(module);
    }

    @Override
    public void register() {
        ScreenRegistry.register(Woodcutters.SCREEN_HANDLER, WoodcutterScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Woodcutters.WOODCUTTER, RenderType.cutout());
    }
}
