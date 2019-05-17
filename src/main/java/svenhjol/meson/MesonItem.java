package svenhjol.meson;

import net.minecraft.item.Item;
import svenhjol.meson.iface.IMesonItem;

@SuppressWarnings("unused")
public abstract class MesonItem extends Item implements IMesonItem
{   
    public MesonItem(String name)
    {
        this.register(name);
    }
}