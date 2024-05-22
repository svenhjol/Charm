package svenhjol.charm.foundation.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

public final class MapHelper {
    public static void drawBackgroundVertex(PoseStack pose, int light, VertexConsumer background) {
        Matrix4f matrix4f = pose.last().pose();
        background.addVertex(matrix4f, -7.0f, 135.0f, 0.0f).setColor(255, 255, 255, 255).setUv(0.0f, 1.0f).setLight(light);
        background.addVertex(matrix4f, 135.0f, 135.0f, 0.0f).setColor(255, 255, 255, 255).setUv(1.0f, 1.0f).setLight(light);
        background.addVertex(matrix4f, 135.0f, -7.0f, 0.0f).setColor(255, 255, 255, 255).setUv(1.0f, 0.0f).setLight(light);
        background.addVertex(matrix4f, -7.0f, -7.0f, 0.0f).setColor(255, 255, 255, 255).setUv(0.0f, 0.0f).setLight(light);
    }
}
