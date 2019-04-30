package svenhjol.meson.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

public class ItemNBTHelper
{
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
