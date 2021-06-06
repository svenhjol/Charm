package svenhjol.charm.helper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import svenhjol.charm.init.CharmResources;

import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;

/**
 * Originally from Quark ShulkerBoxTooltips#renderTooltipBackground()
 * Also copied shulker_widget.png to resources/assets/textures/gui/slot_widget.png
 */
public class TooltipHelper {
    private static final int CORNER = 5;
    private static final int BUFFER = 1;
    private static final int EDGE = 18;

    public static void renderOverlay(PoseStack matrices, List<ItemStack> items, List<ClientTooltipComponent> lines, int tx, int ty) {
        final Minecraft mc = Minecraft.getInstance();

        ty -= 48;

        int x = tx - 5;
        int y = ty - 35;
        int w = 172;
        int h = 27;
        int right = x + w;

        if (right > mc.getWindow().getGuiScaledWidth())
            x -= (right - mc.getWindow().getGuiScaledWidth());

        if (y < 0)
            y = ty + lines.size() * 10 + 5;

        matrices.pushPose();
        RenderSystem.enableDepthTest();
        matrices.translate(0, 0, 400);

        TooltipHelper.renderTooltipBackground(mc, matrices, x, y, 9, 3, -1);

        ItemRenderer render = mc.getItemRenderer();

        float old = render.blitOffset;
        render.blitOffset = 400.0F;
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
                render.renderAndDecorateFakeItem(itemstack, xp, yp);
                render.renderGuiItemDecorations(mc.font, itemstack, xp, yp);
            }
        }
        render.blitOffset = old;

        RenderSystem.disableDepthTest();
        matrices.popPose();
    }

    public static void renderTooltipBackground(Minecraft mc, PoseStack matrix, int x, int y, int width, int height, int color) {
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.setShaderTexture(0, CharmResources.SLOT_WIDGET);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

        GuiComponent.blit(matrix, x, y,
            0, 0, CORNER, CORNER, 256, 256);
        GuiComponent.blit(matrix, x + CORNER + EDGE * width, y + CORNER + EDGE * height,
            CORNER + BUFFER + EDGE + BUFFER, CORNER + BUFFER + EDGE + BUFFER,
            CORNER, CORNER, 256, 256);
        GuiComponent.blit(matrix, x + CORNER + EDGE * width, y,
            CORNER + BUFFER + EDGE + BUFFER, 0,
            CORNER, CORNER, 256, 256);
        GuiComponent.blit(matrix, x, y + CORNER + EDGE * height,
            0, CORNER + BUFFER + EDGE + BUFFER,
            CORNER, CORNER, 256, 256);
        for (int row = 0; row < height; row++) {
            GuiComponent.blit(matrix, x, y + CORNER + EDGE * row,
                0, CORNER + BUFFER,
                CORNER, EDGE, 256, 256);
            GuiComponent.blit(matrix, x + CORNER + EDGE * width, y + CORNER + EDGE * row,
                CORNER + BUFFER + EDGE + BUFFER, CORNER + BUFFER,
                CORNER, EDGE, 256, 256);
            for (int col = 0; col < width; col++) {
                if (row == 0) {
                    GuiComponent.blit(matrix, x + CORNER + EDGE * col, y,
                        CORNER + BUFFER, 0,
                        EDGE, CORNER, 256, 256);
                    GuiComponent.blit(matrix, x + CORNER + EDGE * col, y + CORNER + EDGE * height,
                        CORNER + BUFFER, CORNER + BUFFER + EDGE + BUFFER,
                        EDGE, CORNER, 256, 256);
                }

                GuiComponent.blit(matrix, x + CORNER + EDGE * col, y + CORNER + EDGE * row,
                    CORNER + BUFFER, CORNER + BUFFER,
                    EDGE, EDGE, 256, 256);
            }
        }
    }
}
