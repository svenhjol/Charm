package svenhjol.charm.module.chairs;

import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.annotation.ClientModule;
import svenhjol.charm.loader.CharmModule;

@ClientModule(module = Chairs.class)
public class ChairsClient extends CharmModule {
    @Override
    public void register() {
        EntityRendererRegistry.register(Chairs.CHAIR, ChairEntityRenderer::new);
    }

    public static class ChairEntityRenderer extends EntityRenderer<ChairEntity> {
        public ChairEntityRenderer(EntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public ResourceLocation getTextureLocation(ChairEntity entity) {
            return null;
        }
    }
}
