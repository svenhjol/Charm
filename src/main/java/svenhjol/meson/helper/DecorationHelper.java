package svenhjol.meson.helper;

import net.minecraft.item.Item;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DecorationHelper {
    public static List<Item> FLOWERS = new ArrayList<>();

    public static Item flower(Random random) {
        return FLOWERS.get(random.nextInt(FLOWERS.size()));
    }
}
