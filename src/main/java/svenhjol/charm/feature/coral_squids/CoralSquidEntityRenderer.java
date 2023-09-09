package svenhjol.charm.feature.coral_squids;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CoralSquidEntityRenderer<T extends CoralSquidEntity> extends MobRenderer<T, CoralSquidEntityModel<T>> {
    public CoralSquidEntityRenderer(EntityRendererProvider.Context context, CoralSquidEntityModel<T> model) {
        super(context, model, 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.getTexture();
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
        return Mth.lerp(f, squid.oldTentacleAndle, squid.tentacleAngle);
    }
}
