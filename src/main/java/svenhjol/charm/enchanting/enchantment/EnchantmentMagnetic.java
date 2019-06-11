package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import svenhjol.charm.Charm;
import svenhjol.charm.enchanting.feature.Magnetic;
import svenhjol.meson.MesonEnchantment;

public class EnchantmentMagnetic extends MesonEnchantment
{
    public EnchantmentMagnetic()
    {
        super("magnetic", Rarity.RARE, EnumEnchantmentType.DIGGER, EntityEquipmentSlot.MAINHAND);
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return Magnetic.minEnchantability;
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }

    @Override
    public boolean isTreasureEnchantment()
    {
        return true;
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }
}
