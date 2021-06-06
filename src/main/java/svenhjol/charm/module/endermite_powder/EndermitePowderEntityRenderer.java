package svenhjol.charm.module.endermite_powder;

import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.module.endermite_powder.EndermitePowderEntity;

public class EndermitePowderEntityRenderer extends EntityRenderer<EndermitePowderEntity> {
    public EndermitePowderEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTexture(EndermitePowderEntity entity) {
        return null;
    }
}
