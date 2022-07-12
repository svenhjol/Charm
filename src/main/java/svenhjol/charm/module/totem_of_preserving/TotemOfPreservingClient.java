package svenhjol.charm.module.totem_of_preserving;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.BlockEntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = TotemOfPreserving.class)
public class TotemOfPreservingClient extends CharmModule {
    @Override
    public void register() {
        BlockRenderLayerMap.INSTANCE.putBlock(TotemOfPreserving.BLOCK, RenderType.translucent());
        BlockEntityRendererRegistry.register(TotemOfPreserving.BLOCK_ENTITY, TotemBlockEntityRenderer::new);
    }
}
