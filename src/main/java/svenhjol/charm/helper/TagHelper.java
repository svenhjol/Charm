package svenhjol.charm.helper;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TagHelper {
    public static List<Item> getItemValues(TagKey<Item> tags) {
        List<Item> items = new ArrayList<>();

        var iter = Registry.ITEM.getTagOrEmpty(tags);
        for (Holder<Item> holder : iter) {
            items.add(holder.value());
        }

        return items;
    }

    public static List<Block> getBlockValues(TagKey<Block> tags) {
        List<Block> blocks = new ArrayList<>();

        var iter = Registry.BLOCK.getTagOrEmpty(tags);
        for (Holder<Block> holder : iter) {
            blocks.add(holder.value());
        }

        return blocks;
    }

    public static <T> T getRandomElement(List<T> list, T fallback, Random random) {
        if (list.size() > 0) {
            return list.get(random.nextInt(list.size()));
        }

        return fallback;
    }
}
