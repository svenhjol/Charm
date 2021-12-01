package svenhjol.charm.module.endermite_powder;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class EndermitePowderEntityRenderer extends EntityRenderer<EndermitePowderEntity> {
    public EndermitePowderEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(EndermitePowderEntity entity) {
        return null;
    }
}
