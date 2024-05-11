package svenhjol.charm.foundation.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

public final class MapHelper {
    public static void drawBackgroundVertex(PoseStack pose, int light, VertexConsumer background) {
        Matrix4f matrix4f = pose.last().pose();
        background.vertex(matrix4f, -7.0f, 135.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 1.0f).uv2(light).endVertex();
        background.vertex(matrix4f, 135.0f, 135.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 1.0f).uv2(light).endVertex();
        background.vertex(matrix4f, 135.0f, -7.0f, 0.0f).color(255, 255, 255, 255).uv(1.0f, 0.0f).uv2(light).endVertex();
        background.vertex(matrix4f, -7.0f, -7.0f, 0.0f).color(255, 255, 255, 255).uv(0.0f, 0.0f).uv2(light).endVertex();
    }
}
