package svenhjol.meson;

import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import svenhjol.meson.helper.ItemHelper;

import java.util.List;

public abstract class MesonPotion extends Potion implements IMesonPotion
{
    public String name;

    public MesonPotion(String name, boolean badEffect, int color)
    {
        super(badEffect, color);

        setPotionName(name);
        setRegistryName(new ResourceLocation(getModId(), name));

        this.name = name;

        ProxyRegistry.register(this);
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