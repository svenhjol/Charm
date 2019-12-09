package svenhjol.meson;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import svenhjol.meson.iface.IMesonItem;

public abstract class MesonItem extends Item implements IMesonItem
{
    protected MesonModule module;

    public MesonItem(MesonModule module, String name, Item.Properties props)
    {
        super(props);
        this.module = module;
        register(module, name);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items)
    {
        if (isEnabled()) {
            super.fillItemGroup(group, items);
        }
    }

    @Override
    public boolean isEnabled()
    {
        return module.isEnabled();
    }
}
