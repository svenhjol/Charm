package svenhjol.charm.feature.atlases;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.MapRenderer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.client.renderer.entity.player.PlayerRenderer;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.HumanoidArm;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;

public class AtlasRenderer {
    private final MapRenderer mapItemRenderer;
    private final EntityRenderDispatcher renderManager;

    public AtlasRenderer() {
        var minecraft = Minecraft.getInstance();
        mapItemRenderer = minecraft.gameRenderer.getMapRenderer();
        renderManager = minecraft.getEntityRenderDispatcher();
    }

    public void renderAtlas(PoseStack pose, MultiBufferSource buffers, int light, InteractionHand hand, float equip, float swing, ItemStack stack) {
        var minecraft = Minecraft.getInstance();
        var level = minecraft.level;
        var player = minecraft.player;
        if (player == null) return;

        var inventory = AtlasInventory.get(level, stack);

        pose.pushPose(); // Needed so that parent renderer isn't affect by what we do here.

        var handSide = hand == InteractionHand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();

        // copypasta from renderMapFirstPersonSide
        float e = handSide == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        pose.translate(e * 0.125F, -0.125D, 0.0D);

        // render player arm
        if (!player.isInvisible()) {
            pose.pushPose();
            pose.mulPose(Axis.ZP.rotationDegrees(e * 10.0F));
            renderArm(player, pose, buffers, light, swing, equip, handSide);
            pose.popPose();
        }

        // transform page based on the hand it is held and render it
        pose.pushPose();
        transformPageForHand(pose, buffers, light, swing, equip, handSide);

        int mapId = inventory.getActiveMapId(level);
        MapItemSavedData mapState = inventory.getActiveMap(level);
        renderAtlasMap(mapId, mapState, pose, buffers, light);
        pose.popPose();

        pose.popPose(); // close
    }

    public void renderArm(LocalPlayer player, PoseStack pose, MultiBufferSource buffers, int light, float swing, float equip, HumanoidArm side) {
        // copypasta from renderArmHoldingItem
        var flag = side != HumanoidArm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = Mth.sqrt(swing);
        float f2 = -0.3F * Mth.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * Mth.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * Mth.sin(swing * (float)Math.PI);
        pose.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equip * -0.6F, f4 + -0.71999997F);
        pose.mulPose(Axis.YP.rotationDegrees(f * 45.0F));
        float f5 = Mth.sin(swing * swing * (float)Math.PI);
        float f6 = Mth.sin(f1 * (float)Math.PI);
        pose.mulPose(Axis.YP.rotationDegrees(f * f6 * 70.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(f * f5 * -20.0F));
        RenderSystem.setShaderTexture(0, player.getSkinTextureLocation());
        pose.translate(f * -1.0F, 3.6F, 3.5D);
        pose.mulPose(Axis.ZP.rotationDegrees(f * 120.0F));
        pose.mulPose(Axis.XP.rotationDegrees(200.0F));
        pose.mulPose(Axis.YP.rotationDegrees(f * -135.0F));
        pose.translate(f * 5.6F, 0.0D, 0.0D);
        PlayerRenderer playerrenderer = (PlayerRenderer) renderManager.getRenderer(player);
        if (flag) {
            playerrenderer.renderRightHand(pose, buffers, light, player);
        } else {
            playerrenderer.renderLeftHand(pose, buffers, light, player);
        }
    }

    public void transformPageForHand(PoseStack poseStack, MultiBufferSource buffers, int light, float swing, float equip, HumanoidArm handSide) {
        float e = handSide == HumanoidArm.RIGHT ? 1.0F : -1.0F;
        poseStack.translate((e * 0.51F), (-0.08F + equip * -1.2F), -0.75D);
        float f1 = Mth.sqrt(swing);
        float f2 = Mth.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * Mth.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * Mth.sin(swing * (float) Math.PI);
        poseStack.translate((e * f3), (f4 - 0.3F * f2), f5);
        poseStack.mulPose(Axis.XP.rotationDegrees(f2 * -45.0F));
        poseStack.mulPose(Axis.YP.rotationDegrees(e * f2 * -30.0F));
    }

    public void renderAtlasMap(int mapId, MapItemSavedData mapData, PoseStack pose, MultiBufferSource buffers, int light) {
        this.renderBackground(AtlasesClient.ATLAS_BACKGROUND, pose, buffers, light);

        if (mapData != null) {
            pose.pushPose();
            mapItemRenderer.render(pose, buffers, mapId, mapData, false, light);
            pose.popPose();
        }
    }

    @SuppressWarnings("SameParameterValue")
    private void renderBackground(RenderType background, PoseStack pose, MultiBufferSource buffers, int light) {
        pose.mulPose(Axis.YP.rotationDegrees(180.0F));
        pose.mulPose(Axis.ZP.rotationDegrees(180.0F));
        pose.scale(0.38F, 0.38F, 0.38F);
        pose.translate(-0.5D, -0.5D, 0.0D);
        pose.scale(0.0078125F, 0.0078125F, 0.0078125F);
        AtlasMapHelper.drawBackgroundVertex(pose, light, buffers.getBuffer(background));
    }
}
