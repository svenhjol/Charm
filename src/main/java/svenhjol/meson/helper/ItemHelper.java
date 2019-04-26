package svenhjol.meson.helper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.PotionUtils;
import net.minecraft.util.NonNullList;

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

    public static String getItemStringFromItemStack(ItemStack item, boolean withMeta)
    {
        String itemName = Objects.requireNonNull(item.getItem().getRegistryName()).toString();

        int meta = withMeta ? item.getItemDamage() : '*';
        itemName += "[" + meta + "]";

        return itemName;
    }

    public static List<ItemStack> getItemStacksFromItemString(String name)
    {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        String meta = "";
        if (name.contains("[")) {
            meta = name.substring(name.indexOf('[') + 1, name.indexOf(']'));
            name = name.substring(0, name.indexOf('['));
        }

        // parse meta
        Item item = Item.getByNameOrId(name);
        if (item != null) {
            if (meta.equals("*") || meta.isEmpty()) {
                if (item.getHasSubtypes()) {
                    NonNullList<ItemStack> subItems = NonNullList.create();
                    item.getSubItems(CreativeTabs.SEARCH, subItems);
                    stacks.addAll(subItems);
                } else {
                    stacks.add(new ItemStack(item, 1, 0));
                }
            } else {
                stacks.add(new ItemStack(item, 1, Integer.parseInt(meta)));
            }
        }

        return stacks;
    }

    public static int getInt(ItemStack stack, String tag, int defaultExpected)
    {
        return tagExists(stack, tag) ? getNBT(stack).getInteger(tag) : defaultExpected;
    }

    public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected)
    {
        return tagExists(stack, tag) ? getNBT(stack).getBoolean(tag) : defaultExpected;
    }

    public static double getDouble(ItemStack stack, String tag, double defaultExpected)
    {
        return tagExists(stack, tag) ? getNBT(stack).getDouble(tag) : defaultExpected;
    }

    public static long getLong(ItemStack stack, String tag, long defaultExpected)
    {
        return tagExists(stack, tag) ? getNBT(stack).getLong(tag) : defaultExpected;
    }

    public static void setInt(ItemStack stack, String tag, int i)
    {
        getNBT(stack).setInteger(tag, i);
    }

    public static void setBoolean(ItemStack stack, String tag, boolean b)
    {
        getNBT(stack).setBoolean(tag, b);
    }

    public static void setDouble(ItemStack stack, String tag, double d)
    {
        getNBT(stack).setDouble(tag, d);
    }

    public static void setLong(ItemStack stack, String tag, long l)
    {
        getNBT(stack).setLong(tag, l);
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
