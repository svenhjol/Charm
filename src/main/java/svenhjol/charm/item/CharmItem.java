package svenhjol.charm.item;

import net.minecraft.core.NonNullList;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import svenhjol.charm.item.ICharmItem;
import svenhjol.charm.module.CharmModule;

public abstract class CharmItem extends Item implements ICharmItem {
    protected CharmModule module;

    public CharmItem(CharmModule module, String name, Item.Properties props) {
        super(props);
        this.module = module;
        register(module, name);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if (enabled())
            super.fillItemCategory(group, items);
    }

    @Override
    public boolean enabled() {
        return module.enabled;
    }
}
