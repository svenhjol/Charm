package svenhjol.charm.feature.extra_tooltips;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.iface.ITooltipGrid;

public class ShulkerBoxItemTooltip extends BundleTooltip implements ITooltipGrid {
    public ShulkerBoxItemTooltip(NonNullList<ItemStack> items) {
        super(items, 0);
    }

    @Override
    public int gridSizeX() {
        return 9;
    }

    @Override
    public int gridSizeY() {
        return 3;
    }
}