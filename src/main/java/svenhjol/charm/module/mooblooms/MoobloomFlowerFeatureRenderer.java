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

public class MoobloomFlowerFeatureRenderer<T extends MoobloomEntity> extends RenderLayer<T, CowModel<T>> {
    public MoobloomFlowerFeatureRenderer(RenderLayerParent<T, CowModel<T>> context) {
        super(context);
    }

    // copypasta from MushroomCowMushroomLayer with adjustments to scale and another flower added
    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int light, T entity, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        if (!entity.isBaby() && !entity.isInvisible()) {
            BlockRenderDispatcher blockRenderManager = Minecraft.getInstance().getBlockRenderer();
            BlockState state = entity.getMoobloomType().getFlower();
            int m = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);

            poseStack.pushPose();
            poseStack.translate(0.2D, -0.35D, 0.5D);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
            poseStack.scale(-0.75F, -0.75F, 0.75F);
            poseStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderSingleBlock(state, poseStack, bufferSource, light, m);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.2D, -0.35D, 0.5D);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(42.0F));
            poseStack.translate(0.4D, 0.0D, -0.6D);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
            poseStack.scale(-0.75F, -0.75F, 0.75F);
            poseStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderSingleBlock(state, poseStack, bufferSource, light, m);
            poseStack.popPose();

            poseStack.pushPose();
            poseStack.translate(0.2D, -0.35D, 0.5D);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(42.0F));
            poseStack.translate(-0.05, 0.0D, -0.4D);
            poseStack.mulPose(Vector3f.YP.rotationDegrees(-48.0F));
            poseStack.scale(-0.75F, -0.75F, 0.75F);
            poseStack.translate(-0.5D, -0.65D, -0.5D);
            blockRenderManager.renderSingleBlock(state, poseStack, bufferSource, light, m);
            poseStack.popPose();

            if (entity.isPollinated()) {
                poseStack.pushPose();
                (this.getParentModel()).getHead().translateAndRotate(poseStack);
                poseStack.translate(0.0D, -0.699999988079071D, -0.20000000298023224D);
                poseStack.mulPose(Vector3f.YP.rotationDegrees(-78.0F));
                poseStack.scale(-0.75F, -0.75F, 0.75F);
                poseStack.translate(-0.5D, -0.65D, -0.5D);
                blockRenderManager.renderSingleBlock(state, poseStack, bufferSource, light, m);
                poseStack.popPose();
            }
        }
    }
}
