package svenhjol.charm.module.colored_bundles;

import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DyeColor;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.item.ICharmItem;

public class ColoredBundleItem extends BundleItem implements ICharmItem {
    private final CharmModule module;

    public ColoredBundleItem(CharmModule module, DyeColor color) {
        super((new Item.Settings()).maxCount(1).group(ItemGroup.TOOLS));
        this.register(module, color.getName() + "_bundle");
        this.module = module;
    }

    @Override
    public void appendStacks(ItemGroup group, DefaultedList<ItemStack> items) {
        if (enabled())
            super.appendStacks(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
