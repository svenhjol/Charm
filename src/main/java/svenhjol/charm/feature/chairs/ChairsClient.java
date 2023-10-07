package svenhjol.charm.feature.chairs;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmonyFeature;

@ClientFeature(mod = CharmClient.MOD_ID, feature = Chairs.class)
public class ChairsClient extends CharmonyFeature {
    @Override
    public void register() {
        CharmClient.instance().registry().entityRenderer(Chairs.entity, () -> ChairEntityRenderer::new);
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
