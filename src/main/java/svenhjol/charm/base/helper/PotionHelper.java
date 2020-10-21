package svenhjol.charm.base.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraft.potion.Potions;

public class PotionHelper {
    public static ItemStack getPotionItemStack(Potion potion, int amount) {
        ItemStack out = new ItemStack(Items.POTION, amount);
        PotionUtil.setPotion(out, potion);
        return out;
    }

    public static ItemStack getPotionBottle(int amount, Potion type) {
        ItemStack out = new ItemStack(Items.POTION, amount);
        PotionUtil.setPotion(out, type);
        return out;
    }

    public static ItemStack getFilledWaterBottle() {
        return getFilledWaterBottle(1);
    }

    public static ItemStack getFilledWaterBottle(int amount) {
        return getPotionBottle(amount, Potions.WATER);
    }

}
