package svenhjol.charm.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.render.*;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Matrix4f;
import java.util.function.Consumer;

public class MapRenderHelper {
    private static final RenderType MAP_BACKGROUND = RenderType.text(new ResourceLocation("textures/map/map_background.png"));

    public static void renderMapWithBackground(PoseStack matrices, int x, int y, int z, float scale, int light, Consumer<MultiBufferSource.BufferSource> renderMap) {
        matrices.pushPose();
        matrices.translate(x, y, z);
        matrices.scale(scale, scale, 1);
        //see also MapTooltipsClient
        MultiBufferSource.BufferSource bufferSource = Minecraft.getInstance().renderBuffers().bufferSource();
        drawBackgroundVertex(matrices, light, bufferSource.getBuffer(MAP_BACKGROUND));
        renderMap.accept(bufferSource);
        bufferSource.endBatch();
        matrices.popPose();
    }

    public static void drawBackgroundVertex(PoseStack matrices, int light, VertexConsumer background) {
        Matrix4f matrix4f = matrices.last().pose();
        background.vertex(matrix4f, -7.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 1.0F).uv2(light).endVertex();
        background.vertex(matrix4f, 135.0F, 135.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 1.0F).uv2(light).endVertex();
        background.vertex(matrix4f, 135.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(1.0F, 0.0F).uv2(light).endVertex();
        background.vertex(matrix4f, -7.0F, -7.0F, 0.0F).color(255, 255, 255, 255).uv(0.0F, 0.0F).uv2(light).endVertex();
    }
}
