package svenhjol.charm.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.util.math.MatrixStack;
import svenhjol.charm.base.CharmResources;

/**
 * Copypasta from Quark ShulkerBoxTooltips#renderTooltipBackground()
 * Also copied shulker_widget.png to resources/assets/textures/gui/slot_widget.png
 */
public class TooltipInventoryHandler {
    private static final int CORNER = 5;
    private static final int BUFFER = 1;
    private static final int EDGE = 18;

    public static void renderTooltipBackground(MinecraftClient mc, MatrixStack matrix, int x, int y, int width, int height, int color) {
        mc.getTextureManager().bindTexture(CharmResources.SLOT_WIDGET);
        RenderSystem.color3f(((color & 0xFF0000) >> 16) / 255f,
            ((color & 0x00FF00) >> 8) / 255f,
            (color & 0x0000FF) / 255f);

        DiffuseLighting.disable();

        DrawableHelper.drawTexture(matrix, x, y,
            0, 0, CORNER, CORNER, 256, 256);
        DrawableHelper.drawTexture(matrix, x + CORNER + EDGE * width, y + CORNER + EDGE * height,
            CORNER + BUFFER + EDGE + BUFFER, CORNER + BUFFER + EDGE + BUFFER,
            CORNER, CORNER, 256, 256);
        DrawableHelper.drawTexture(matrix, x + CORNER + EDGE * width, y,
            CORNER + BUFFER + EDGE + BUFFER, 0,
            CORNER, CORNER, 256, 256);
        DrawableHelper.drawTexture(matrix, x, y + CORNER + EDGE * height,
            0, CORNER + BUFFER + EDGE + BUFFER,
            CORNER, CORNER, 256, 256);
        for (int row = 0; row < height; row++) {
            DrawableHelper.drawTexture(matrix, x, y + CORNER + EDGE * row,
                0, CORNER + BUFFER,
                CORNER, EDGE, 256, 256);
            DrawableHelper.drawTexture(matrix, x + CORNER + EDGE * width, y + CORNER + EDGE * row,
                CORNER + BUFFER + EDGE + BUFFER, CORNER + BUFFER,
                CORNER, EDGE, 256, 256);
            for (int col = 0; col < width; col++) {
                if (row == 0) {
                    DrawableHelper.drawTexture(matrix, x + CORNER + EDGE * col, y,
                        CORNER + BUFFER, 0,
                        EDGE, CORNER, 256, 256);
                    DrawableHelper.drawTexture(matrix, x + CORNER + EDGE * col, y + CORNER + EDGE * height,
                        CORNER + BUFFER, CORNER + BUFFER + EDGE + BUFFER,
                        EDGE, CORNER, 256, 256);
                }

                DrawableHelper.drawTexture(matrix, x + CORNER + EDGE * col, y + CORNER + EDGE * row,
                    CORNER + BUFFER, CORNER + BUFFER,
                    EDGE, EDGE, 256, 256);
            }
        }

        RenderSystem.color3f(1F, 1F, 1F);
    }
}
