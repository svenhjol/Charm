package svenhjol.charm.feature.chairs;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charmony.client.ClientFeature;
import svenhjol.charmony.common.CommonFeature;

public class ChairsClient extends ClientFeature {
    @Override
    public Class<? extends CommonFeature> commonFeature() {
        return Chairs.class;
    }

    @Override
    public void register() {
        mod().registry().entityRenderer(Chairs.entity, () -> ChairEntityRenderer::new);
    }

    static class ChairEntityRenderer extends EntityRenderer<ChairEntity> {
        public ChairEntityRenderer(EntityRendererProvider.Context context) {
            super(context);
        }

        @Override
        public ResourceLocation getTextureLocation(ChairEntity entity) {
            return null;
        }
    }
}
