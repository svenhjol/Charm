package svenhjol.charm.feature.extra_tooltips.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.foundation.screen.CharmTooltip;

import java.util.List;

/**
 * Copypasta from {@link net.minecraft.client.gui.screens.inventory.tooltip.ClientBundleTooltip}
 */
public record ShulkerBoxItemTooltip(List<ItemStack> items) implements ClientTooltipComponent, CharmTooltip {
    @Override
    public int gridSizeX() {
        return 9;
    }

    @Override
    public int gridSizeY() {
        return 3;
    }

    @Override
    public List<ItemStack> getItems() {
        return items;
    }

    @Override
    public int getHeight() {
        return this.gridSizeY() * SLOT_SIZE_Y + MARGIN_Y;
    }

    @Override
    public int getWidth(Font font) {
        return this.gridSizeX() * SLOT_SIZE_X + 2;
    }

    @Override
    public void renderImage(Font font, int i, int j, GuiGraphics guiGraphics) {
        defaultRenderImage(font, i, j, guiGraphics);
    }
}