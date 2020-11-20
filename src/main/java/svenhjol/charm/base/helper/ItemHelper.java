package svenhjol.charm.base.helper;

import net.minecraft.block.Block;
import net.minecraft.entity.ItemEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import svenhjol.charm.mixin.accessor.ItemEntityAccessor;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class ItemHelper {
    public static Map<Item, Integer> ITEM_LIFETIME = new HashMap<>();

    public static Class<? extends Block> getBlockClass(ItemStack stack) {
        return Block.getBlockFromItem(stack.getItem()).getClass();
    }

    public static int getAmountWithLooting(Random rand, int maxDrops, int lootingLevel, float lootingBoost) {
        int amount = rand.nextInt(Math.max(1, maxDrops + 1));
        if (rand.nextFloat() < lootingBoost * lootingLevel)
            amount++;

        return amount;
    }

    public static boolean shouldItemDespawn(ItemEntity itemEntity) {
        Item item = itemEntity.getStack().getItem();
        if (ItemHelper.ITEM_LIFETIME.containsKey(item)) {
            if (((ItemEntityAccessor)itemEntity).getAge() < ItemHelper.ITEM_LIFETIME.get(item))
                return false;
        }

        return true;
    }
}
