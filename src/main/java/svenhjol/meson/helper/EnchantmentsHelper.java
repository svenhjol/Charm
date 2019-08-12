package svenhjol.meson.helper;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;

public class EnchantmentsHelper
{
    public static boolean hasEnchantment(Enchantment enchantment, ItemStack stack)
    {
        if (stack.getItem() instanceof EnchantedBookItem) {
            /* @todo books must be handled separately */
            return false;
        } else {
            return EnchantmentHelper.getEnchantmentLevel(enchantment, stack) > 0;
        }
    }
}
