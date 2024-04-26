package svenhjol.charm.feature.chairs;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

class ChairEntityRenderer extends EntityRenderer<ChairEntity> {
    public ChairEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ChairEntity entity) {
        return null;
    }
}
