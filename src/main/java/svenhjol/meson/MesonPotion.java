package svenhjol.meson;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import svenhjol.meson.helper.ItemHelper;
import svenhjol.meson.iface.IMesonPotion;

import java.util.List;

public abstract class MesonPotion extends Potion implements IMesonPotion
{
    public String name;

    public MesonPotion(String name, boolean badEffect, int color)
    {
        super(badEffect, color);
        this.register(name);
        this.name = name;
    }

    @Override
    public List<ItemStack> getCurativeItems()
    {
        return ItemHelper.getCurativeItems();
    }

    @Override
    public String getName()
    {
        return getModId() + ".potion." + this.name + ".name";
    }
}