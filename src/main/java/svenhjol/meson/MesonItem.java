package svenhjol.meson;

import net.minecraft.item.Item;

@SuppressWarnings("unused")
public abstract class MesonItem extends Item implements IMesonItem
{   
    public MesonItem(String name)
    {
        this.register(name);
    }
}