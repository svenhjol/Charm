package svenhjol.charm.helper;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Matrix4f;

import java.util.function.Consumer;

public class MapRenderHelper {
    private static final RenderLayer MAP_BACKGROUND = RenderLayer.getText(new Identifier("textures/map/map_background.png"));

    public static void renderMapWithBackground(MatrixStack matrices, int x, int y, int z, float scale, int light, Consumer<VertexConsumerProvider.Immediate> renderMap) {
        matrices.push();
        matrices.translate(x, y, z);
        matrices.scale(scale, scale, 1);
        //see also MapTooltipsClient
        VertexConsumerProvider.Immediate bufferSource = MinecraftClient.getInstance().getBufferBuilders().getEntityVertexConsumers();
        drawBackgroundVertex(matrices, light, bufferSource.getBuffer(MAP_BACKGROUND));
        renderMap.accept(bufferSource);
        bufferSource.draw();
        matrices.pop();
    }

    public static void drawBackgroundVertex(MatrixStack matrices, int light, VertexConsumer background) {
        Matrix4f matrix4f = matrices.peek().getModel();
        background.vertex(matrix4f, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).texture(0.0F, 1.0F).light(light).next();
        background.vertex(matrix4f, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).texture(1.0F, 1.0F).light(light).next();
        background.vertex(matrix4f, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).texture(1.0F, 0.0F).light(light).next();
        background.vertex(matrix4f, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).texture(0.0F, 0.0F).light(light).next();
    }
}
