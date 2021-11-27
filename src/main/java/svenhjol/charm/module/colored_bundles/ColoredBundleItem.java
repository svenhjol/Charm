package svenhjol.charm.module.colored_bundles;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.BundleItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.item.ICharmItem;
import svenhjol.charm.loader.CharmModule;

public class ColoredBundleItem extends BundleItem implements ICharmItem {
    private final CharmModule module;

    public ColoredBundleItem(CharmModule module, DyeColor color) {
        super((new Properties()).stacksTo(1).tab(CreativeModeTab.TAB_TOOLS));
        this.register(module, color.getName() + "_bundle");
        this.module = module;
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled()) {
            super.fillItemCategory(group, items);
        }
    }

    @Override
    public boolean enabled() {
        return module.isEnabled();
    }
}
