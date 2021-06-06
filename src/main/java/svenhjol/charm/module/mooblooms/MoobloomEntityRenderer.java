package svenhjol.charm.module.mooblooms;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import svenhjol.charm.module.mooblooms.MoobloomsClient;
import svenhjol.charm.module.mooblooms.MoobloomEntity;
import svenhjol.charm.module.mooblooms.MoobloomFlowerFeatureRenderer;

@Environment(EnvType.CLIENT)
public class MoobloomEntityRenderer extends MobRenderer<MoobloomEntity, CowModel<MoobloomEntity>> {
    public MoobloomEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new CowModel<>(context.bakeLayer(MoobloomsClient.LAYER)), 0.7F);
        this.addLayer(new MoobloomFlowerFeatureRenderer<>(this));
    }

    @Override
    public ResourceLocation getTexture(MoobloomEntity entity) {
        return entity.getMoobloomTexture();
    }
}
