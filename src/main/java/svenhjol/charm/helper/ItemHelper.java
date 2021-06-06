package svenhjol.charm.helper;

import svenhjol.charm.mixin.accessor.ItemEntityAccessor;

import java.util.*;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;

public class ItemHelper {
    public static Map<Item, Integer> ITEM_LIFETIME = new HashMap<>();
    public static List<Item> BOWL_FOODS = Arrays.asList(
        Items.MUSHROOM_STEW,
        Items.RABBIT_STEW,
        Items.BEETROOT_SOUP,
        Items.SUSPICIOUS_STEW
    );
    public static List<Item> BOTTLE_FOODS = Arrays.asList(
        Items.HONEY_BOTTLE
    );

    public static Class<? extends Block> getBlockClass(ItemStack stack) {
        return Block.byItem(stack.getItem()).getClass();
    }

    public static int getAmountWithLooting(Random rand, int maxDrops, int lootingLevel, float lootingBoost) {
        int amount = rand.nextInt(Math.max(1, maxDrops + 1));
        if (rand.nextFloat() < lootingBoost * lootingLevel)
            amount++;

        return amount;
    }

    public static boolean shouldItemDespawn(ItemEntity itemEntity) {
        Item item = itemEntity.getItem().getItem();
        if (ItemHelper.ITEM_LIFETIME.containsKey(item)) {
            if (((ItemEntityAccessor)itemEntity).getAge() < ItemHelper.ITEM_LIFETIME.get(item))
                return false;
        }

        return true;
    }

    public static List<Item> getBowlFoodItems() {
        return BOWL_FOODS;
    }

    public static List<Item> getBottleFoodItems() {
        return BOTTLE_FOODS;
    }
}
