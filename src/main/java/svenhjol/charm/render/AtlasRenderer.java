package svenhjol.charm.render;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.MapRenderer;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.item.ItemStack;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3f;
import svenhjol.charm.Charm;
import svenhjol.charm.base.helper.MapRenderHelper;
import svenhjol.charm.module.Atlases;
import svenhjol.charm.screenhandler.AtlasInventory;

public class AtlasRenderer {
    public final RenderLayer ATLAS_BACKGROUND = RenderLayer.getText(new Identifier(Charm.MOD_ID, "textures/map/atlas_background.png"));
    private final MapRenderer mapItemRenderer;
    private final EntityRenderDispatcher renderManager;
    private final TextureManager textureManager;

    public AtlasRenderer() {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        mapItemRenderer = minecraft.gameRenderer.getMapRenderer();
        renderManager = minecraft.getEntityRenderDispatcher();
        textureManager = minecraft.getTextureManager();
    }

    public void renderAtlas(MatrixStack matrixStack, VertexConsumerProvider buffers, int light, Hand hand, float equip, float swing, ItemStack stack) {
        MinecraftClient minecraft = MinecraftClient.getInstance();
        ClientWorld world = minecraft.world;
        ClientPlayerEntity player = minecraft.player;
        if (player == null) return;
        AtlasInventory inventory = Atlases.getInventory(world, stack);

        matrixStack.push(); // needed so that parent renderer isn't affect by what we do here

        Arm handSide = hand == Hand.MAIN_HAND ? player.getMainArm() : player.getMainArm().getOpposite();

        // copypasta from renderMapFirstPersonSide
        float e = handSide == Arm.RIGHT ? 1.0F : -1.0F;
        matrixStack.translate(e * 0.125F, -0.125D, 0.0D);

        // render player arm
        if (!player.isInvisible()) {
            matrixStack.push();
            matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(e * 10.0F));
            renderArm(player, matrixStack, buffers, light, swing, equip, handSide);
            matrixStack.pop();
        }

        // transform page based on the hand it is held and render it
        matrixStack.push();
        transformPageForHand(matrixStack, buffers, light, swing, equip, handSide);

        int mapId = inventory.getActiveMapId(world);
        MapState mapState = inventory.getActiveMap(world);
        renderAtlasMap(mapId, mapState, matrixStack, buffers, light);
        matrixStack.pop();

        matrixStack.pop(); // close
    }

    public void renderArm(ClientPlayerEntity player, MatrixStack matrixStack, VertexConsumerProvider buffers, int light, float swing, float equip, Arm side) {
        // copypasta from remderArmHoldingItem
        boolean flag = side != Arm.LEFT;
        float f = flag ? 1.0F : -1.0F;
        float f1 = MathHelper.sqrt(swing);
        float f2 = -0.3F * MathHelper.sin(f1 * (float)Math.PI);
        float f3 = 0.4F * MathHelper.sin(f1 * ((float)Math.PI * 2F));
        float f4 = -0.4F * MathHelper.sin(swing * (float)Math.PI);
        matrixStack.translate(f * (f2 + 0.64000005F), f3 + -0.6F + equip * -0.6F, f4 + -0.71999997F);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(f * 45.0F));
        float f5 = MathHelper.sin(swing * swing * (float)Math.PI);
        float f6 = MathHelper.sin(f1 * (float)Math.PI);
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(f * f6 * 70.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(f * f5 * -20.0F));
        RenderSystem.setShaderTexture(0, player.getSkinTexture());
        matrixStack.translate(f * -1.0F, 3.6F, 3.5D);
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(f * 120.0F));
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(200.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(f * -135.0F));
        matrixStack.translate(f * 5.6F, 0.0D, 0.0D);
        PlayerEntityRenderer playerrenderer = (PlayerEntityRenderer) renderManager.getRenderer(player);
        if (flag) {
            playerrenderer.renderRightArm(matrixStack, buffers, light, player);
        } else {
            playerrenderer.renderLeftArm(matrixStack, buffers, light, player);
        }
    }

    public void transformPageForHand(MatrixStack matrixStack, VertexConsumerProvider buffers, int light, float swing, float equip, Arm handSide) {
        float e = handSide == Arm.RIGHT ? 1.0F : -1.0F;
        matrixStack.translate((e * 0.51F), (-0.08F + equip * -1.2F), -0.75D);
        float f1 = MathHelper.sqrt(swing);
        float f2 = MathHelper.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * MathHelper.sin(swing * (float) Math.PI);
        matrixStack.translate((e * f3), (f4 - 0.3F * f2), f5);
        matrixStack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(f2 * -45.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(e * f2 * -30.0F));
    }

    public void renderAtlasMap(int mapId, MapState mapData, MatrixStack matrixStack, VertexConsumerProvider buffers, int light) {
        this.renderBackground(ATLAS_BACKGROUND, matrixStack, buffers, light);

        if (mapData != null) {
            matrixStack.push();
            mapItemRenderer.draw(matrixStack, buffers, mapId, mapData, false, light);
            matrixStack.pop();
        }
    }

    private void renderBackground(RenderLayer background, MatrixStack matrixStack, VertexConsumerProvider buffers, int light) {
        matrixStack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
        matrixStack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
        matrixStack.scale(0.38F, 0.38F, 0.38F);
        matrixStack.translate(-0.5D, -0.5D, 0.0D);
        matrixStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
        MapRenderHelper.drawBackgroundVertex(matrixStack, light, buffers.getBuffer(background));
    }
}
