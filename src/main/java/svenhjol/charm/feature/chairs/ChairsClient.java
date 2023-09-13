package svenhjol.charm.feature.chairs;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.Charm;
import svenhjol.charm.CharmClient;
import svenhjol.charmony.annotation.ClientFeature;
import svenhjol.charmony.base.CharmFeature;

import java.util.List;
import java.util.function.BooleanSupplier;

@ClientFeature
public class ChairsClient extends CharmFeature {
    @Override
    public List<BooleanSupplier> checks() {
        return List.of(() -> Charm.instance().loader().isEnabled(Chairs.class));
    }

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
