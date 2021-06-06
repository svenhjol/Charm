package svenhjol.charm.helper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

public class PotionHelper {
    public static ItemStack getPotionItemStack(Potion potion, int amount) {
        ItemStack out = new ItemStack(Items.POTION, amount);
        PotionUtils.setPotion(out, potion);
        return out;
    }

    public static ItemStack getPotionBottle(int amount, Potion type) {
        ItemStack out = new ItemStack(Items.POTION, amount);
        PotionUtils.setPotion(out, type);
        return out;
    }

    public static ItemStack getFilledWaterBottle() {
        return getFilledWaterBottle(1);
    }

    public static ItemStack getFilledWaterBottle(int amount) {
        return getPotionBottle(amount, Potions.WATER);
    }
}
