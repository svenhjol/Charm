package svenhjol.meson.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;

public class PotionHelper
{
    public static ItemStack getPotionItemStack(Potion potion, int amount)
    {
        ItemStack out = new ItemStack(Items.POTION, amount);
        PotionUtils.addPotionToItemStack(out, potion);
        return out;
    }
}
