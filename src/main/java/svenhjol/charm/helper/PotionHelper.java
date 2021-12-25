package svenhjol.charm.helper;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;

/**
 * @version 4.0.0-charm
 */
public class PotionHelper {
    public static ItemStack getPotionBottle(Potion potion, int amount) {
        var out = new ItemStack(Items.POTION, amount);
        PotionUtils.setPotion(out, potion);
        return out;
    }

    public static ItemStack getSplashPotionBottle(Potion potion, int amount) {
        var out = new ItemStack(Items.SPLASH_POTION, amount);
        PotionUtils.setPotion(out, potion);
        return out;
    }

    public static ItemStack getLingeringPotionBottle(Potion potion, int amount) {
        var out = new ItemStack(Items.LINGERING_POTION, amount);
        PotionUtils.setPotion(out, potion);
        return out;
    }

    public static ItemStack getFilledWaterBottle(int amount) {
        return getPotionBottle(Potions.WATER, 1);
    }
}
