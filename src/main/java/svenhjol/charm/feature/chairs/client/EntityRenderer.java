package svenhjol.charm.feature.chairs.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.feature.chairs.common.Chair;

class EntityRenderer extends net.minecraft.client.renderer.entity.EntityRenderer<Chair> {
    public EntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(Chair entity) {
        return null;
    }
}
