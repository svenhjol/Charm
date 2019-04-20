package svenhjol.meson.helper;

import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    public static boolean compareItemString(ItemStack item, String itemString)
    {
        ItemStack fromItemString = getItemStackFromItemString(itemString);
        if (fromItemString == null) {
            return false;
        }

        return item.getItem() == fromItemString.getItem() && item.getItemDamage() == fromItemString.getItemDamage();
    }

    public static String getItemStringFromItemStack(ItemStack item)
    {
        String itemName = Objects.requireNonNull(item.getItem().getRegistryName()).toString();
        int meta = item.getItemDamage();
        if (meta > 0) {
            itemName += "[" + meta + "]";
        }

        return itemName;
    }

    public static ItemStack getItemStackFromItemString(String itemString)
    {
        int meta = 0;
        if (itemString.contains("[")) {
            meta = Integer.parseInt(itemString.substring(itemString.indexOf('[') + 1, itemString.indexOf(']')));
            itemString = itemString.substring(0, itemString.indexOf('['));
        }

        Item item = Item.getByNameOrId(itemString);
        if (item != null) {
            return new ItemStack(item, 1, meta);
        }

        return null;
    }

    public static int getInt(ItemStack stack, String tag, int defaultExpected)
    {
        return tagExists(stack, tag) ? getNBT(stack).getInteger(tag) : defaultExpected;
    }

    public static void setInt(ItemStack stack, String tag, int i)
    {
        getNBT(stack).setInteger(tag, i);
    }

    public static NBTTagCompound getNBT(ItemStack stack)
    {
        if (!stack.hasTagCompound()) {
            stack.setTagCompound(new NBTTagCompound());
        }
        return stack.getTagCompound();
    }

    public static boolean tagExists(ItemStack stack, String tag)
    {
        return !stack.isEmpty() && stack.hasTagCompound() && getNBT(stack).hasKey(tag);
    }
}
