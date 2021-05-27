package svenhjol.charm.module.mooblooms;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.util.Identifier;
import svenhjol.charm.module.mooblooms.MoobloomsClient;
import svenhjol.charm.module.mooblooms.MoobloomEntity;
import svenhjol.charm.module.mooblooms.MoobloomFlowerFeatureRenderer;

@Environment(EnvType.CLIENT)
public class MoobloomEntityRenderer extends MobEntityRenderer<MoobloomEntity, CowEntityModel<MoobloomEntity>> {
    public MoobloomEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new CowEntityModel<>(context.getPart(MoobloomsClient.LAYER)), 0.7F);
        this.addFeature(new MoobloomFlowerFeatureRenderer<>(this));
    }

    @Override
    public Identifier getTexture(MoobloomEntity entity) {
        return entity.getMoobloomTexture();
    }
}
