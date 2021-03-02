package svenhjol.charm.handler;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.render.DiffuseLighting;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.text.OrderedText;
import svenhjol.charm.base.CharmResources;

import java.util.List;

/**
 * Copypasta from Quark ShulkerBoxTooltips#renderTooltipBackground()
 * Also copied shulker_widget.png to resources/assets/textures/gui/slot_widget.png
 */
public class TooltipInventoryHandler {
    private static final int CORNER = 5;
    private static final int BUFFER = 1;
    private static final int EDGE = 18;

    public static void renderOverlay(MatrixStack matrices, List<ItemStack> items, List<? extends OrderedText> lines, int tx, int ty) {
        final MinecraftClient mc = MinecraftClient.getInstance();

        ty -= 48;

        int x = tx - 5;
        int y = ty - 35;
        int w = 172;
        int h = 27;
        int right = x + w;

        if (right > mc.getWindow().getScaledWidth())
            x -= (right - mc.getWindow().getScaledWidth());

        if (y < 0)
            y = ty + lines.size() * 10 + 5;

        RenderSystem.pushMatrix();
        DiffuseLighting.enable();
        RenderSystem.enableRescaleNormal();
        RenderSystem.color3f(1f, 1f, 1f);
        RenderSystem.translatef(0, 0, 700);
        mc.getTextureManager().bindTexture(CharmResources.SLOT_WIDGET);

        DiffuseLighting.disable();
        TooltipInventoryHandler.renderTooltipBackground(mc, matrices, x, y, 9, 3, -1);
        RenderSystem.color3f(1f, 1f, 1f);

        ItemRenderer render = mc.getItemRenderer();
        DiffuseLighting.enable();
        RenderSystem.enableDepthTest();

        for (int i = 0; i < items.size(); i++) {
            ItemStack itemstack;

            try {
                itemstack = items.get(i);
            } catch (Exception e) {
                // catch null issue with itemstack. Needs investigation. #255
                continue;
            }
            int xp = x + 6 + (i % 9) * 18;
            int yp = y + 6 + (i / 9) * 18;

            if (!itemstack.isEmpty()) {
                render.renderGuiItemIcon(itemstack, xp, yp);
                render.renderGuiItemOverlay(mc.textRenderer, itemstack, xp, yp);
            }
        }

        RenderSystem.disableDepthTest();
        RenderSystem.disableRescaleNormal();
        RenderSystem.popMatrix();
    }

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
