package svenhjol.meson;

import net.minecraft.item.Item;

public abstract class MesonItem extends Item implements IMesonItem
{   
    public MesonItem(String name)
    {
        this.register(name);
    }
}