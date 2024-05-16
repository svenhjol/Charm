package svenhjol.charm.feature.mooblooms.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.CowModel;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.PinkPetalsBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import svenhjol.charm.feature.mooblooms.common.FlowerBlockState;
import svenhjol.charm.feature.mooblooms.common.Moobloom;

public class FlowerRenderer<T extends Moobloom> extends RenderLayer<T, CowModel<T>> {
    public FlowerRenderer(RenderLayerParent<T, CowModel<T>> context) {
        super(context);
    }

    /**
     * Copypasta from MushroomCowMushroomLayer with adjustments to scale and another flower added.
     */
    @Override
    public void render(PoseStack pose, MultiBufferSource bufferSource, int light, T entity, float limbAngle,
                       float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch){
        if (!entity.isBaby() && !entity.isInvisible()) {
            var coords = LivingEntityRenderer.getOverlayCoords(entity, 0.0f);
            var dispatcher = Minecraft.getInstance().getBlockRenderer();
            var flower = entity.getMoobloomType().getFlower();
            var state = flower.getBlockState();

            if (flower.equals(FlowerBlockState.PINK_PETALS)) {
                renderCherryBlossomMoobloom(pose, state, dispatcher, bufferSource, light, coords, entity);
                return;
            } else if (flower.equals(FlowerBlockState.SUNFLOWER)) {
                state = state.setValue(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER);
            }

            renderDefaultMoobloom(pose, state, dispatcher, bufferSource, light, coords, entity);
        }
    }

    public void renderDefaultMoobloom(PoseStack pose, BlockState state, BlockRenderDispatcher dispatcher,
                                      MultiBufferSource bufferSource, int light, int coords, T entity) {
        pose.pushPose();
        pose.translate(0.2d, -0.35d, 0.5d);
        pose.mulPose(Axis.YP.rotationDegrees(-48.0f));
        pose.scale(-0.75f, -0.75f, 0.75f);
        pose.translate(-0.5d, -0.65d, -0.5d);
        dispatcher.renderSingleBlock(state, pose, bufferSource, light, coords);
        pose.popPose();

        pose.pushPose();
        pose.translate(0.2d, -0.35d, 0.5d);
        pose.mulPose(Axis.YP.rotationDegrees(42.0f));
        pose.translate(0.4d, 0.0d, -0.6d);
        pose.mulPose(Axis.YP.rotationDegrees(-48.0f));
        pose.scale(-0.75f, -0.75f, 0.75f);
        pose.translate(-0.5d, -0.65d, -0.5d);
        dispatcher.renderSingleBlock(state, pose, bufferSource, light, coords);
        pose.popPose();

        pose.pushPose();
        pose.translate(0.2d, -0.35d, 0.5d);
        pose.mulPose(Axis.YP.rotationDegrees(42.0f));
        pose.translate(-0.05, 0.0d, -0.4d);
        pose.mulPose(Axis.YP.rotationDegrees(-48.0f));
        pose.scale(-0.75f, -0.75f, 0.75f);
        pose.translate(-0.5d, -0.65d, -0.5d);
        dispatcher.renderSingleBlock(state, pose, bufferSource, light, coords);
        pose.popPose();

        if (entity.isPollinated()) {
            pose.pushPose();
            (this.getParentModel()).getHead().translateAndRotate(pose);
            pose.translate(0.0d, -0.7d, -0.2d);
            pose.mulPose(Axis.YP.rotationDegrees(-78.0f));
            pose.scale(-0.75f, -0.75f, 0.75f);
            pose.translate(-0.5d, -0.65d, -0.5d);
            dispatcher.renderSingleBlock(state, pose, bufferSource, light, coords);
            pose.popPose();
        }
    }

    public void renderCherryBlossomMoobloom(PoseStack pose, BlockState state, BlockRenderDispatcher dispatcher,
                                            MultiBufferSource bufferSource, int light, int coords, T entity) {
        var x = -0.25d;
        var y = -0.15d;
        var z = -0.25d;

        if (entity.isPollinated()) {
            state = state.setValue(PinkPetalsBlock.AMOUNT, 4);
            x = -0.48d;
            z = -0.48d;
        }

        pose.pushPose();
        pose.translate(0d, 0d, 0d);
        pose.mulPose(Axis.YP.rotationDegrees(-48.0f));
        pose.scale(-0.75f, -0.75f, 0.75f);
        pose.translate(x, y, z);
        dispatcher.renderSingleBlock(state, pose, bufferSource, light, coords);
        pose.popPose();
    }
}
