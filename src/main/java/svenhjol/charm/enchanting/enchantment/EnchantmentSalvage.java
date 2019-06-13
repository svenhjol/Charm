package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import svenhjol.charm.Charm;
import svenhjol.charm.enchanting.feature.Salvage;
import svenhjol.meson.MesonEnchantment;

public class EnchantmentSalvage extends MesonEnchantment
{
    public EnchantmentSalvage()
    {
        super("salvage", Rarity.COMMON, EnumEnchantmentType.BREAKABLE, EntityEquipmentSlot.MAINHAND);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return Salvage.minEnchantability;
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }
}