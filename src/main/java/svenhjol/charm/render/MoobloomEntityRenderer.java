package svenhjol.charm.render;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.util.Identifier;
import svenhjol.charm.entity.MoobloomEntity;

@Environment(EnvType.CLIENT)
public class MoobloomEntityRenderer extends MobEntityRenderer<MoobloomEntity, CowEntityModel<MoobloomEntity>> {
    public MoobloomEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new CowEntityModel<>(), 0.7F);
        this.addFeature(new MoobloomFlowerFeatureRenderer<>(this));
    }

    @Override
    public Identifier getTexture(MoobloomEntity entity) {
        return entity.getMoobloomTexture();
    }
}
