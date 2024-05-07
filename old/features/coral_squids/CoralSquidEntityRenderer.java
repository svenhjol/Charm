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
        return entity.getVariant().getTexture();
    }

    /**
     * Copypasta from SquidRenderer.
     */
    @Override
    protected void setupRotations(T squid, PoseStack pose, float f, float g, float h, float i) {
        var j = Mth.lerp(h, squid.xBodyRot0, squid.xBodyRot);
        var k = Mth.lerp(h, squid.zBodyRot0, squid.zBodyRot);
        pose.translate(0.0d, 0.25d, 0.0d);
        pose.mulPose(Axis.YP.rotationDegrees(180.0f - g));
        pose.mulPose(Axis.XP.rotationDegrees(j));
        pose.mulPose(Axis.YP.rotationDegrees(k));
        pose.translate(0.0d, -0.6d, 0.0d);
    }

    @Override
    protected float getBob(T squid, float f) {
        return Mth.lerp(f, squid.oldTentacleAngle, squid.tentacleAngle);
    }
}
