package svenhjol.charm.item;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.charm.module.CharmModule;

public abstract class CharmItem extends Item implements ICharmItem {
    protected CharmModule module;

    public CharmItem(CharmModule module, String name, Item.Settings props) {
        super(props);
        this.module = module;
        register(module, name);
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
