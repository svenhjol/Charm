package svenhjol.charm.render;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import svenhjol.charm.entity.CoralSquidEntity;
import svenhjol.charm.model.CoralSquidEntityModel;

public class CoralSquidEntityRenderer extends MobEntityRenderer<CoralSquidEntity, CoralSquidEntityModel<CoralSquidEntity>> {
    public CoralSquidEntityRenderer(EntityRenderDispatcher entityRenderDispatcher) {
        super(entityRenderDispatcher, new CoralSquidEntityModel<>(), 0.7F);
    }

    @Override
    public Identifier getTexture(CoralSquidEntity entity) {
        return entity.getTexture();
    }

    /**
     * Copypasta from SquidEntityRenderer.
     */
    protected void setupTransforms(CoralSquidEntity squidEntity, MatrixStack matrixStack, float f, float g, float h) {
        float i = MathHelper.lerp(h, squidEntity.prevTiltAngle, squidEntity.tiltAngle);
        float j = MathHelper.lerp(h, squidEntity.prevRollAngle, squidEntity.rollAngle);
        matrixStack.translate(0.0D, 0.25D, 0.0D);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F - g));
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(i));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(j));
        matrixStack.translate(0.0D, -1.2000000476837158D, 0.0D);
    }

    protected float getAnimationProgress(CoralSquidEntity squidEntity, float f) {
        return MathHelper.lerp(f, squidEntity.prevTentacleAngle, squidEntity.tentacleAngle);
    }
}
