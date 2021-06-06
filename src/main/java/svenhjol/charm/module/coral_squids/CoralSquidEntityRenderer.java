package svenhjol.charm.module.coral_squids;

import svenhjol.charm.module.coral_squids.CoralSquidsClient;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import svenhjol.charm.module.coral_squids.CoralSquidEntity;
import svenhjol.charm.module.coral_squids.CoralSquidEntityModel;

public class CoralSquidEntityRenderer extends MobRenderer<CoralSquidEntity, CoralSquidEntityModel<CoralSquidEntity>> {
    public CoralSquidEntityRenderer(EntityRendererProvider.Context context) {
        super(context, new CoralSquidEntityModel<>(context.bakeLayer(CoralSquidsClient.LAYER)), 0.7F);
    }

    @Override
    public ResourceLocation getTexture(CoralSquidEntity entity) {
        return entity.getTexture();
    }

    /**
     * Copypasta from SquidEntityRenderer.
     */
    protected void setupTransforms(CoralSquidEntity squidEntity, PoseStack matrixStack, float f, float g, float h) {
        float i = Mth.lerp(h, squidEntity.prevTiltAngle, squidEntity.tiltAngle);
        float j = Mth.lerp(h, squidEntity.prevRollAngle, squidEntity.rollAngle);
        matrixStack.translate(0.0D, 0.25D, 0.0D);
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(180.0F - g));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(i));
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(j));
        matrixStack.translate(0.0D, -1.2000000476837158D, 0.0D);
    }

    protected float getAnimationProgress(CoralSquidEntity squidEntity, float f) {
        return Mth.lerp(f, squidEntity.prevTentacleAngle, squidEntity.tentacleAngle);
    }
}
