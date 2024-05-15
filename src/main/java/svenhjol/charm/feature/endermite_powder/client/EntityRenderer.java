package svenhjol.charm.feature.endermite_powder.client;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.feature.endermite_powder.common.Entity;

import javax.annotation.Nullable;

public class EntityRenderer extends net.minecraft.client.renderer.entity.EntityRenderer<Entity> {
    protected EntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Nullable
    @Override
    public ResourceLocation getTextureLocation(Entity entity) {
        return null;
    }
}
