package svenhjol.charm.feature.mooblooms;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;

public class MoobloomFlowerFeatureRenderer<T extends MoobloomEntity> extends RenderLayer<T, CowModel<T>> {
    public MoobloomFlowerFeatureRenderer(RenderLayerParent<T, CowModel<T>> context) {
        super(context);
    }

    /**
     * Copypasta from MushroomCowMushroomLayer with adjustments to scale and another flower added.
     */
    @Override
    public void render(PoseStack pose, MultiBufferSource bufferSource, int light, T entity, float limbAngle,
                       float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch){
        if (!entity.isBaby() && !entity.isInvisible()) {
            var coords = LivingEntityRenderer.getOverlayCoords(entity, 0.0F);
            var dispatcher = Minecraft.getInstance().getBlockRenderer();
            var state = entity.getMoobloomType().getFlower();

            if (state.getBlock() == Blocks.PINK_PETALS) {
                renderPinkPetalMoobloom(pose, state, dispatcher, bufferSource, light, coords, entity);
            } else {
                renderDefaultMoobloom(pose, state, dispatcher, bufferSource, light, coords, entity);
            }
        }
    }

    public void renderDefaultMoobloom(PoseStack pose, BlockState state, BlockRenderDispatcher dispatcher,
                                      MultiBufferSource bufferSource, int light, int coords, T entity) {
        pose.pushPose();
        pose.translate(0.2D, -0.35D, 0.5D);
        pose.mulPose(Axis.YP.rotationDegrees(-48.0F));
        pose.scale(-0.75F, -0.75F, 0.75F);
        pose.translate(-0.5D, -0.65D, -0.5D);
        dispatcher.renderSingleBlock(state, pose, bufferSource, light, coords);
        pose.popPose();

        pose.pushPose();
        pose.translate(0.2D, -0.35D, 0.5D);
        pose.mulPose(Axis.YP.rotationDegrees(42.0F));
        pose.translate(0.4D, 0.0D, -0.6D);
        pose.mulPose(Axis.YP.rotationDegrees(-48.0F));
        pose.scale(-0.75F, -0.75F, 0.75F);
        pose.translate(-0.5D, -0.65D, -0.5D);
        dispatcher.renderSingleBlock(state, pose, bufferSource, light, coords);
        pose.popPose();

        pose.pushPose();
        pose.translate(0.2D, -0.35D, 0.5D);
        pose.mulPose(Axis.YP.rotationDegrees(42.0F));
        pose.translate(-0.05, 0.0D, -0.4D);
        pose.mulPose(Axis.YP.rotationDegrees(-48.0F));
        pose.scale(-0.75F, -0.75F, 0.75F);
        pose.translate(-0.5D, -0.65D, -0.5D);
        dispatcher.renderSingleBlock(state, pose, bufferSource, light, coords);
        pose.popPose();

        if (entity.isPollinated()) {
            pose.pushPose();
            (this.getParentModel()).getHead().translateAndRotate(pose);
            pose.translate(0.0D, -0.7D, -0.2D);
            pose.mulPose(Axis.YP.rotationDegrees(-78.0F));
            pose.scale(-0.75F, -0.75F, 0.75F);
            pose.translate(-0.5D, -0.65D, -0.5D);
            dispatcher.renderSingleBlock(state, pose, bufferSource, light, coords);
            pose.popPose();
        }
    }

    public void renderPinkPetalMoobloom(PoseStack pose, BlockState state, BlockRenderDispatcher dispatcher,
                                        MultiBufferSource bufferSource, int light, int coords, T entity) {
        var x = -0.25D;
        var y = -0.15D;
        var z = -0.25D;

        if (entity.isPollinated()) {
            state = state.setValue(PinkPetalsBlock.AMOUNT, 4);
            x = -0.48D;
            z = -0.48D;
        }

        pose.pushPose();
        pose.translate(0D, 0D, 0D);
        pose.mulPose(Axis.YP.rotationDegrees(-48.0F));
        pose.scale(-0.75F, -0.75F, 0.75F);
        pose.translate(x, y, z);
        dispatcher.renderSingleBlock(state, pose, bufferSource, light, coords);
        pose.popPose();
    }
}
