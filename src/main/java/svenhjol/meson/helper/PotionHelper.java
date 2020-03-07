package svenhjol.meson.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtils;
import net.minecraft.potion.Potions;

public class PotionHelper {
    public static ItemStack getPotionItemStack(Potion potion, int amount) {
        ItemStack out = new ItemStack(Items.POTION, amount);
        PotionUtils.addPotionToItemStack(out, potion);
        return out;
    }

    public static ItemStack getPotionBottle(int amount, Potion type) {
        ItemStack out = new ItemStack(Items.POTION, amount);
        PotionUtils.addPotionToItemStack(out, type);
        return out;
    }

    public static ItemStack getFilledWaterBottle() {
        return getFilledWaterBottle(1);
    }

    public static ItemStack getFilledWaterBottle(int amount) {
        return getPotionBottle(amount, Potions.WATER);
    }

}
