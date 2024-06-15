package svenhjol.charm.feature.totem_of_preserving.client;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

import java.util.List;

/**
 * @see ClientTooltipComponent#create(TooltipComponent)
 */
public record Tooltip(List<ItemStack> items) implements ClientTooltipComponent, CharmTooltip {
    @Override
    public int getWidth(Font font) {
        return this.gridSizeX() * SLOT_SIZE_X + 2;
    }

    @Override
    public int getHeight() {
        return this.gridSizeY() * SLOT_SIZE_Y + MARGIN_Y;
    }

    @Override
    public int gridSizeX() {
        return Math.max(9, (int)Math.ceil(Math.sqrt((double)this.items.size() + 1.0)));
    }

    @Override
    public int gridSizeY() {
        return (int)Math.ceil(((double)this.items.size() + 1.0) / (double)this.gridSizeX());
    }

    @Override
    public List<ItemStack> getItems() {
        return items;
    }

    @Override
    public void renderImage(Font font, int i, int j, GuiGraphics guiGraphics) {
        defaultRenderImage(font, i, j, guiGraphics);
    }
}
