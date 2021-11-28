package svenhjol.charm.module.shulker_box_tooltip;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.client.CharmItemTooltip;
import svenhjol.charm.client.ICustomGridsize;

public class ShulkerBoxItemTooltip extends CharmItemTooltip implements ICustomGridsize {
    public ShulkerBoxItemTooltip(NonNullList<ItemStack> items) {
        super(items);
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
