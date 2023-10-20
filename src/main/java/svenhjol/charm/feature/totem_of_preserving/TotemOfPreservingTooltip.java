package svenhjol.charm.feature.totem_of_preserving;

import net.minecraft.core.NonNullList;
import net.minecraft.world.inventory.tooltip.BundleTooltip;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.ItemStack;

/**
 * We have to extend BundleTooltip because there's a hardcoded check in
 * ClientTooltipComponent that will crash the game if a different type is provided.
 * @see net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent#create(TooltipComponent)
 */
public class TotemOfPreservingTooltip extends BundleTooltip {
    public TotemOfPreservingTooltip(NonNullList<ItemStack> items) {
        super(items, 0);
    }
}
