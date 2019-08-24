package svenhjol.charm.enchanting.enchantment;

import net.minecraft.enchantment.EnchantmentType;
import net.minecraft.inventory.EquipmentSlotType;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import svenhjol.meson.MesonEnchantment;
import svenhjol.meson.MesonModule;

public class HomingEnchantment extends MesonEnchantment
{
    public HomingEnchantment(MesonModule module)
    {
        super(module, "homing",
            Rarity.RARE, EnchantmentType.BREAKABLE, EquipmentSlotType.MAINHAND);
    }

    @Override
    public boolean isTreasureEnchantment()
    {
        return true;
    }

    @Override
    public int getMaxLevel()
    {
        return 1;
    }

    @Override
    public int getMinEnchantability(int enchantmentLevel)
    {
        return 15;
    }

    @Override
    public boolean canApply(ItemStack stack)
    {
        return (stack.getItem() instanceof HoeItem);
    }
}
