package svenhjol.charm.feature.coral_squids.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import svenhjol.charm.feature.coral_squids.common.CoralSquid;

public class EntityRenderer<T extends CoralSquid> extends MobRenderer<T, Model<T>> {
    public EntityRenderer(EntityRendererProvider.Context context, Model<T> model) {
        super(context, model, 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.getVariant().getTexture();
    }

    /**
     * Copypasta from SquidRenderer.
     */
    @Override
    protected void setupRotations(T squid, PoseStack pose, float f, float g, float h) {
        var i = Mth.lerp(h, squid.xBodyRot0, squid.xBodyRot);
        var j = Mth.lerp(h, squid.zBodyRot0, squid.zBodyRot);
        pose.translate(0.0D, 0.25D, 0.0D);
        pose.mulPose(Axis.YP.rotationDegrees(180.0F - g));
        pose.mulPose(Axis.XP.rotationDegrees(i));
        pose.mulPose(Axis.YP.rotationDegrees(j));
        pose.translate(0.0D, -0.6D, 0.0D);
    }

    @Override
    protected float getBob(T squid, float f) {
        return Mth.lerp(f, squid.oldTentacleAngle, squid.tentacleAngle);
    }
}
