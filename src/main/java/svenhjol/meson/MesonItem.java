package svenhjol.meson;

import net.minecraft.item.Item;
import svenhjol.meson.iface.IMesonItem;

public abstract class MesonItem extends Item implements IMesonItem
{
    public MesonItem(String name, Item.Properties properties)
    {
        super(properties);
        register(name);
    }
}
