package svenhjol.charm.render;

import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.CowEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import svenhjol.charm.entity.MoobloomEntity;

public class MoobloomFlowerFeatureRenderer<T extends MoobloomEntity> extends FeatureRenderer<T, CowEntityModel<T>> {
    public MoobloomFlowerFeatureRenderer(FeatureRendererContext<T, CowEntityModel<T>> context) {
        super(context);
    }

    // copypasta from MooshroomMushroomFeatureRenderer with adjustments to scale and another flower added
    @Override
    public void render(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.isBaby() && !entity.isInvisible()) {
            BlockRenderManager blockRenderManager = MinecraftClient.getInstance().getBlockRenderManager();
            BlockState state = entity.getMoobloomType().getFlower();
            int m = LivingEntityRenderer.getOverlay(entity, 0.0F);


            matrixStack.push();
            matrixStack.translate(0.2D, -0.35D, 0.5D);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-48.0F));
            matrixStack.scale(-0.75F, -0.75F, 0.75F);
            matrixStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderBlockAsEntity(state, matrixStack, vertexConsumerProvider, light, m);
            matrixStack.pop();


            matrixStack.push();
            matrixStack.translate(0.2D, -0.35D, 0.5D);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(42.0F));
            matrixStack.translate(0.4D, 0.0D, -0.6D);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-48.0F));
            matrixStack.scale(-0.75F, -0.75F, 0.75F);
            matrixStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderBlockAsEntity(state, matrixStack, vertexConsumerProvider, light, m);
            matrixStack.pop();


            matrixStack.push();
            matrixStack.translate(0.2D, -0.35D, 0.5D);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(42.0F));
            matrixStack.translate(-0.05, 0.0D, -0.4D);
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-48.0F));
            matrixStack.scale(-0.75F, -0.75F, 0.75F);
            matrixStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderBlockAsEntity(state, matrixStack, vertexConsumerProvider, light, m);
            matrixStack.pop();


            if (entity.isPollinated()) {
                matrixStack.push();
                (this.getContextModel()).getHead().rotate(matrixStack);
                matrixStack.translate(0.0D, -0.699999988079071D, -0.20000000298023224D);
                matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(-78.0F));
                matrixStack.scale(-0.75F, -0.75F, 0.75F);
                matrixStack.translate(-0.5D, -0.65D, -0.5D);
                blockRenderManager.renderBlockAsEntity(state, matrixStack, vertexConsumerProvider, light, m);
                matrixStack.pop();
            }
        }
    }
}
