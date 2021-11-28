package svenhjol.charm.module.shulker_box_tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.client.ICustomGridsize;

public class ShulkerBoxItemTooltip extends BundleTooltip implements ICustomGridsize {
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
