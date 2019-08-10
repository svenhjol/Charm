package svenhjol.meson;

import net.minecraft.item.Item;
import svenhjol.meson.iface.IMesonItem;

public abstract class MesonItem extends Item implements IMesonItem
{
    public MesonItem(Item.Properties props)
    {
        super(props);
    }
}
