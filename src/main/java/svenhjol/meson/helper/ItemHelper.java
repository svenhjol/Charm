package svenhjol.meson.helper;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.init.PotionTypes;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.PotionType;
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
        return getPotionBottle(amount, PotionTypes.WATER);
    }

    public static ItemStack getPotionBottle(int amount, PotionType type)
    {
        ItemStack out = new ItemStack(Items.POTIONITEM, amount);
        PotionUtils.addPotionToItemStack(out, type);
        return out;
    }

    public static boolean compareStacks(ItemStack s1, ItemStack s2)
    {
        return s1.getItem() == s2.getItem() && (s1.getMetadata() == 32767 || s1.getMetadata() == s2.getMetadata());
    }

    public static String getMatchingItemKey(List<String> items, ItemStack item)
    {
        String name = getItemStringFromItemStack(item, true);

        // literal match
        if (items.contains(name)) return name;

        if (items.contains(name + "[*]")) return name + "[*]";

        String meta = "";
        if (name.contains("[")) {
            meta = name.substring(name.indexOf('[') + 1, name.indexOf(']'));
            name = name.substring(0, name.indexOf('['));
        }

        // matches after meta is stripped?
        if (items.contains(name)) return name;

        if (!meta.isEmpty() && items.contains(name + "[*]")) {
            return name + "[*]";
        }

        return "";
    }

    public static String getItemStringFromItemStack(ItemStack item, boolean withMeta)
    {
        String itemName = Objects.requireNonNull(item.getItem().getRegistryName()).toString();

        int meta = withMeta ? item.getItemDamage() : '*';
        itemName += "[" + meta + "]";

        return itemName;
    }

    public static ItemStack getItemStackFromItemString(String name)
    {
        return getItemStackFromItemString(name, 0);
    }

    public static ItemStack getItemStackFromItemString(String name, int defaultMeta)
    {
        ItemStack stack = null;
        String meta = "";
        if (name.contains("[")) {
            meta = name.substring(name.indexOf('[') + 1, name.indexOf(']'));
            name = name.substring(0, name.indexOf('['));
        }

        // parse meta
        Item item = Item.getByNameOrId(name);
        if (item != null) {
            if (meta.equals("*") || meta.isEmpty()) {
                stack = new ItemStack(item, 1, defaultMeta);
            } else {
                stack = new ItemStack(item, 1, Integer.parseInt(meta));
            }
        }

        return stack;
    }

    public static List<ItemStack> getItemStacksFromItemString(String name)
    {
        return getItemStacksFromItemString(name, 0);
    }

    public static List<ItemStack> getItemStacksFromItemString(String name, int defaultMeta)
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
                    stacks.add(new ItemStack(item, 1, defaultMeta));
                }
            } else {
                stacks.add(new ItemStack(item, 1, Integer.parseInt(meta)));
            }
        }

        return stacks;
    }
}
