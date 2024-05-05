package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.foundation.screen.CharmTooltip;

import java.util.List;

/**
 * @see net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent#create(TooltipComponent)
 */
public record TotemOfPreservingTooltip(List<ItemStack> items) implements ClientTooltipComponent, CharmTooltip {
    @Override
    public int getWidth(Font font) {
        return this.gridSizeX() * 18 + 2;
    }

    @Override
    public int getHeight() {
        return this.gridSizeY() * 20 + 2;
    }

    @Override
    public int gridSizeX() {
        return Math.max(2, (int)Math.ceil(Math.sqrt((double)this.items.size() + 1.0)));
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
