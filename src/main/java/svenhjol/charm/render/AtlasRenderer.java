package svenhjol.charm.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector3f;
import net.minecraft.item.map.MapState;
import net.minecraft.util.Hand;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Matrix4f;
import svenhjol.charm.Charm;
import svenhjol.charm.screenhandler.AtlasInventory;

import java.util.Map;
import java.util.WeakHashMap;

public class AtlasRenderer implements AutoCloseable {
    public final RenderLayer ATLAS_BACKGROUND = RenderLayer.getText(new Identifier(Charm.MOD_ID, "textures/map/atlas_background.png"));
    public final TextureManager textureManager;
    public final Map<AtlasInventory, Instance> instances = new WeakHashMap<>();

    public AtlasRenderer(TextureManager textureManager) {
        this.textureManager = textureManager;
    }

    public Instance getInstance(AtlasInventory inventory) {
        Instance i = this.instances.get(inventory);
        if (i == null) {
            i = new Instance();
            this.instances.put(inventory, i);
        }
        return i;
    }

    public void clearInstances() {
        for (Instance i : this.instances.values()) {
            i.close();
        }
        this.instances.clear();
    }

    @Override
    public void close() {
        this.clearInstances();
    }

    public void renderAtlas(ClientPlayerEntity player, AtlasInventory inventory, MatrixStack matrixStack, VertexConsumerProvider buffers, int light) {
        this.getInstance(inventory).renderAtlas(inventory.getActiveMap(player), matrixStack, buffers, light);
    }

    public void renderArm(ClientPlayerEntity player, MatrixStack matrixStack, VertexConsumerProvider buffers, int light, float swing, float equip, Hand hand) {
        // render arm
        float e = hand == Hand.MAIN_HAND ? 1.0F : -1.0F;

        // copypasta from renderArmFirstPerson
        float e1 = MathHelper.sqrt(swing);
        float e2 = -0.3F * MathHelper.sin(e1 * (float) Math.PI);
        float e3 = 0.4F * MathHelper.sin(e1 * ((float) Math.PI * 2F));
        float e4 = -0.4F * MathHelper.sin(swing * (float) Math.PI);
        matrixStack.translate((double) (e * (e2 + 0.64000005F)), (double) (e3 + -0.6F + equip * -0.6F), (double) (e4 + -0.71999997F));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(e * 45.0F));
        float e5 = MathHelper.sin(swing * swing * (float) Math.PI);
        float e6 = MathHelper.sin(e1 * (float) Math.PI);
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(e * e6 * 70.0F));
        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(e * e5 * -20.0F));
        MinecraftClient.getInstance().getTextureManager().bindTexture(player.getSkinTexture());
        matrixStack.translate((double) (e * -1.0F), (double) 3.6F, 3.5D);
        matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(e * 120.0F));
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(200.0F));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(e * -135.0F));
        matrixStack.translate((double) (e * 5.6F), 0.0D, 0.0D);
        PlayerEntityRenderer playerrenderer = (PlayerEntityRenderer) MinecraftClient.getInstance().getEntityRenderDispatcher().getRenderer(player);
        if (hand == Hand.MAIN_HAND) {
            playerrenderer.renderRightArm(matrixStack, buffers, light, player);
        } else {
            playerrenderer.renderLeftArm(matrixStack, buffers, light, player);
        }
    }

    public void transformPageForHand(MatrixStack matrixStack, VertexConsumerProvider buffers, int light, float swing, float equip, Hand hand) {
        float e = hand == Hand.MAIN_HAND ? 1.0F : -1.0F;
        matrixStack.translate((double) (e * 0.51F), (double) (-0.08F + equip * -1.2F), -0.75D);
        float f1 = MathHelper.sqrt(swing);
        float f2 = MathHelper.sin(f1 * (float) Math.PI);
        float f3 = -0.5F * f2;
        float f4 = 0.4F * MathHelper.sin(f1 * ((float) Math.PI * 2F));
        float f5 = -0.3F * MathHelper.sin(swing * (float) Math.PI);
        matrixStack.translate((double) (e * f3), (double) (f4 - 0.3F * f2), (double) f5);
        matrixStack.multiply(Vector3f.POSITIVE_X.getDegreesQuaternion(f2 * -45.0F));
        matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(e * f2 * -30.0F));
    }

    public class Instance implements AutoCloseable {

        private Instance() {
        }

        public void renderAtlas(MapState mapData, MatrixStack matrixStack, VertexConsumerProvider buffers, int light) {
            this.renderBackground(matrixStack, buffers, light);

            if (mapData != null) {
                matrixStack.push();
                MinecraftClient.getInstance().gameRenderer.getMapRenderer().draw(matrixStack, buffers, mapData, false, light);
                matrixStack.pop();
            }
        }

        private void renderBackground(MatrixStack matrixStack, VertexConsumerProvider buffers, int light) {
            this.renderBackground(ATLAS_BACKGROUND, matrixStack, buffers, light);
        }

        // see HeldItemRenderer#renderFirstPersonMap
        private void renderBackground(RenderLayer background, MatrixStack matrixStack, VertexConsumerProvider buffers, int light) {
            matrixStack.multiply(Vector3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
            matrixStack.multiply(Vector3f.POSITIVE_Z.getDegreesQuaternion(180.0F));
            matrixStack.scale(0.38F, 0.38F, 0.38F);
            matrixStack.translate(-0.5D, -0.5D, 0.0D);
            matrixStack.scale(0.0078125F, 0.0078125F, 0.0078125F);
            VertexConsumer builder = buffers.getBuffer(background);
            Matrix4f matrix4f = matrixStack.peek().getModel();
            builder.vertex(matrix4f, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).texture(0.0F, 1.0F).light(light).next();
            builder.vertex(matrix4f, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).texture(1.0F, 1.0F).light(light).next();
            builder.vertex(matrix4f, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).texture(1.0F, 0.0F).light(light).next();
            builder.vertex(matrix4f, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).texture(0.0F, 0.0F).light(light).next();
        }

        @Override
        public void close() {

        }
    }
}
