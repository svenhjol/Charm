package svenhjol.charm.feature.mooblooms;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;

public class MoobloomEntityRenderer<T extends MoobloomEntity> extends MobRenderer<T, CowModel<T>> {
    public MoobloomEntityRenderer(EntityRendererProvider.Context context, CowModel<T> model) {
        super(context, model, 0.7F);
        this.addLayer(new MoobloomFlowerFeatureRenderer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.getMoobloomTexture();
    }
}
