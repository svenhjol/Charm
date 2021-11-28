package svenhjol.charm.client;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.item.ItemStack;

public class CharmItemTooltip extends BundleTooltip {
    public CharmItemTooltip(NonNullList<ItemStack> items) {
        super(items, 0);
    }
}
