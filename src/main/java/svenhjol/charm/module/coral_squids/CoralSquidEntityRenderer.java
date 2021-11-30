package svenhjol.charm.module.coral_squids;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class CoralSquidEntityRenderer<T extends CoralSquidEntity> extends MobRenderer<T, CoralSquidEntityModel<T>> {
    public CoralSquidEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new CoralSquidEntityModel<>(context.bakeLayer(CoralSquidsClient.LAYER)), 0.7F);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return entity.getTexture();
    }

    /**
     * Copypasta from SquidRenderer.
     */
    @Override
    protected void setupRotations(T squid, PoseStack poseStack, float f, float g, float h) {
        float i = Mth.lerp(h, squid.xBodyRot0, squid.xBodyRot);
        float j = Mth.lerp(h, squid.zBodyRot0, squid.zBodyRot);
        poseStack.translate(0.0D, 0.25D, 0.0D);
        poseStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - g));
        poseStack.mulPose(Vector3f.XP.rotationDegrees(i));
        poseStack.mulPose(Vector3f.YP.rotationDegrees(j));
        poseStack.translate(0.0D, -0.6D, 0.0D);
    }

    @Override
    protected float getBob(T squid, float f) {
        return Mth.lerp(f, squid.oldTentacleAndle, squid.tentacleAngle);
    }
}
