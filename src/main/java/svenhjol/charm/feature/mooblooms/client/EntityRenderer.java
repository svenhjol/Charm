package svenhjol.charm.feature.mooblooms.client;

import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.feature.mooblooms.common.Moobloom;

public class EntityRenderer<T extends Moobloom> extends MobRenderer<T, CowModel<T>> {
    public EntityRenderer(EntityRendererProvider.Context context, CowModel<T> model) {
        super(context, model, 0.7f);
        this.addLayer(new FlowerRenderer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.getMoobloomTexture();
    }
}
