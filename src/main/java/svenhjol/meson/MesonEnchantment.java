package svenhjol.meson;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import svenhjol.meson.iface.IMesonEnchantment;

public abstract class MesonEnchantment extends Enchantment implements IMesonEnchantment
{
    public MesonEnchantment(Rarity rarity, EnchantmentType type, EquipmentSlotType... slots)
    {
        super(rarity, type, slots);
    }
}
