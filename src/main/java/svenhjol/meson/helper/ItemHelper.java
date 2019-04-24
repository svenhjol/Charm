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
                ItemStack itemHolder = new ItemStack(item);
                for (int i = 0; i < itemHolder.getMaxDamage(); i++) {
                    ItemStack copy = itemHolder.copy();
                    copy.setItemDamage(i);
                    stacks.add(copy);
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
