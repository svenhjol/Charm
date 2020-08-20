package svenhjol.meson.helper;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;

import java.util.Random;

public class ItemHelper {
    public static Class<? extends Block> getBlockClass(ItemStack stack) {
        return Block.getBlockFromItem(stack.getItem()).getClass();
    }

    public static int getAmountWithLooting(Random rand, int maxDrops, int lootingLevel, float lootingBoost) {
        int amount = rand.nextInt(Math.max(1, maxDrops + 1));
        if (rand.nextFloat() < lootingBoost * lootingLevel)
            amount++;

        return amount;
    }
}
