package svenhjol.charm.module.woodcutters;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.helper.ClientRegistryHelper;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = Woodcutters.class)
public class WoodcuttersClient extends CharmModule {

    @Override
    public void register() {
        ClientRegistryHelper.screenHandler(Woodcutters.SCREEN_HANDLER, WoodcutterScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Woodcutters.WOODCUTTER, RenderType.cutout());
    }
}
