package svenhjol.charm.loot.item;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import svenhjol.charm.Charm;
import svenhjol.charm.loot.feature.TotemOfShielding;
import svenhjol.meson.iface.IMesonItem;

public class ItemTotemOfShielding extends Item implements IMesonItem
{
    public ItemTotemOfShielding()
    {
        register("totem_of_shielding");
        setMaxStackSize(1);
        setMaxDamage(TotemOfShielding.maxHealth);
        setCreativeTab(CreativeTabs.COMBAT);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public boolean isEnchantable(ItemStack stack)
    {
        return false;
    }
}