package svenhjol.charm.module.woodcutters;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;
import svenhjol.charm.registry.ClientRegistry;

@ClientModule(module = Woodcutters.class)
public class WoodcuttersClient extends CharmModule {

    @Override
    public void register() {
        ClientRegistry.menuScreen(Woodcutters.MENU, WoodcutterScreen::new);
        BlockRenderLayerMap.INSTANCE.putBlock(Woodcutters.WOODCUTTER, RenderType.cutout());
    }
}
