package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.inventory.EntityEquipmentSlot;
import svenhjol.charm.Charm;
import svenhjol.meson.MesonEnchantment;

public class EnchantmentMagnetic extends MesonEnchantment
{
    public EnchantmentMagnetic()
    {
        super("magnetic", Rarity.RARE, EnumEnchantmentType.DIGGER, EntityEquipmentSlot.MAINHAND);
    }

    @Override
    public String getModId()
    {
        return Charm.MOD_ID;
    }
}
