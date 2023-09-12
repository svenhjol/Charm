package svenhjol.charm.feature.endermite_powder;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

import javax.annotation.Nullable;

public class EndermitePowderEntityRenderer extends EntityRenderer<EndermitePowderEntity> {
    protected EndermitePowderEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(EndermitePowderEntity entity) {
        return null;
    }
}
