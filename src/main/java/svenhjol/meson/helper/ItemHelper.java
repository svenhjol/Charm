package svenhjol.meson.helper;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ItemHelper
{
    public static List<ItemStack> availableTotems = new ArrayList<ItemStack>() {{
        new ItemStack(Items.TOTEM_OF_UNDYING);
    }};
    public static List<ItemStack> curativeItems = new ArrayList<ItemStack>() {{
        new ItemStack(Items.MILK_BUCKET);
    }};

    public static List<ItemStack> getCurativeItems()
    {
        return curativeItems.stream().distinct().collect(Collectors.toList());
    }

    public static ItemStack getFilledWaterBottle()
    {
        return getFilledWaterBottle(1);
    }

    public static ItemStack getFilledWaterBottle(int amount)
    {
        ItemStack out = new ItemStack(Items.POTIONITEM, amount);
        PotionUtils.addPotionToItemStack(out, PotionTypes.WATER);
        return out;
    }
}
