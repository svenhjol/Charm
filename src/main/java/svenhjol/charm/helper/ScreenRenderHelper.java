package svenhjol.charm.helper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.mixin.accessor.AbstractContainerScreenAccessor;

/**
 * @version 1.0.0-charm
 */
public class ScreenRenderHelper {
    public static IconRenderer iconRenderer;

    public static int getX(AbstractContainerScreen<?> screen) {
        return ((AbstractContainerScreenAccessor)screen).getX();
    }

    public static int getY(AbstractContainerScreen<?> screen) {
        return ((AbstractContainerScreenAccessor)screen).getY();
    }

    public static IconRenderer getIconRenderer() {
        if (iconRenderer == null)
            iconRenderer = new IconRenderer();

        return iconRenderer;
    }

    public static void drawCenteredTitle(PoseStack matrices, Component title, int left, int top, int color) {
        ClientHelper.getTextRenderer().ifPresent(font
            -> GuiComponent.drawCenteredString(matrices, font, title, left, top, color));
    }

    public static void renderItemStack(ItemStack stack, int x, int y) {
        ClientHelper.getItemRenderer().ifPresent(item
            -> item.renderGuiItem(stack, x, y));
    }

    public static void renderIcon(Screen screen, PoseStack matrices, ResourceLocation texture, int[] icon, int x, int y, int width, int height) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        screen.blit(matrices, x, y, 256 - (icon[0] * width), icon[1] * height, width, height);
    }

    public static class IconRenderer extends GuiComponent {
        public void renderGuiIcon(PoseStack pose, int drawX, int drawY, int offsetX, int offsetY, int sizeX, int sizeY) {
            RenderSystem.setShaderTexture(0, GuiComponent.GUI_ICONS_LOCATION);
            this.blit(pose, drawX, drawY, offsetX, offsetY, sizeX, sizeY);
        }
    }
}
