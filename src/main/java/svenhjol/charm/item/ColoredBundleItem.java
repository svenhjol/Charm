package svenhjol.charm.item;

import net.minecraft.item.BundleItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.util.DyeColor;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.item.ICharmItem;

public class ColoredBundleItem extends BundleItem implements ICharmItem {
    private CharmModule module;

    public ColoredBundleItem(CharmModule module, DyeColor color) {
        super((new Item.Settings()).maxCount(1).group(ItemGroup.TOOLS));
        this.register(module, color.getName() + "_bundle");
        this.module = module;
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
