package svenhjol.meson.item;

import net.minecraft.item.*;
import net.minecraft.util.collection.DefaultedList;
import svenhjol.meson.MesonModule;

public abstract class MesonItem extends Item implements IMesonItem {
    protected MesonModule module;

    public MesonItem(MesonModule module, String name, Item.Settings props) {
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
