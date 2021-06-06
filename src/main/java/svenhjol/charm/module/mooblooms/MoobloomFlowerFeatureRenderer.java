package svenhjol.charm.module.mooblooms;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.level.block.state.BlockState;
import svenhjol.charm.module.mooblooms.MoobloomEntity;

public class MoobloomFlowerFeatureRenderer<T extends MoobloomEntity> extends RenderLayer<T, CowModel<T>> {
    public MoobloomFlowerFeatureRenderer(RenderLayerParent<T, CowModel<T>> context) {
        super(context);
    }

    // copypasta from MooshroomMushroomFeatureRenderer with adjustments to scale and another flower added
    @Override
    public void render(PoseStack matrixStack, MultiBufferSource vertexConsumerProvider, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.isBaby() && !entity.isInvisible()) {
            BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
            BlockState state = entity.getMoobloomType().getFlower();
            int m = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);


            matrixStack.pushPose();
            matrixStack.translate(0.2D, -0.35D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
            matrixStack.scale(-0.75F, -0.75F, 0.75F);
            matrixStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderSingleBlock(state, matrixStack, vertexConsumerProvider, light, m);
            matrixStack.popPose();


            matrixStack.pushPose();
            matrixStack.translate(0.2D, -0.35D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(42.0F));
            matrixStack.translate(0.4D, 0.0D, -0.6D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
            matrixStack.scale(-0.75F, -0.75F, 0.75F);
            matrixStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderSingleBlock(state, matrixStack, vertexConsumerProvider, light, m);
            matrixStack.popPose();


            matrixStack.pushPose();
            matrixStack.translate(0.2D, -0.35D, 0.5D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(42.0F));
            matrixStack.translate(-0.05, 0.0D, -0.4D);
            matrixStack.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
            matrixStack.scale(-0.75F, -0.75F, 0.75F);
            matrixStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderSingleBlock(state, matrixStack, vertexConsumerProvider, light, m);
            matrixStack.popPose();


            if (entity.isPollinated()) {
                matrixStack.pushPose();
                (this.getParentModel()).getHead().translateAndRotate(matrixStack);
                matrixStack.translate(0.0D, -0.699999988079071D, -0.20000000298023224D);
                matrixStack.mulPose(Vector3f.YP.rotationDegrees(-78.0F));
                matrixStack.scale(-0.75F, -0.75F, 0.75F);
                matrixStack.translate(-0.5D, -0.65D, -0.5D);
                blockRenderManager.renderSingleBlock(state, matrixStack, vertexConsumerProvider, light, m);
                matrixStack.popPose();
            }
        }
    }
}
