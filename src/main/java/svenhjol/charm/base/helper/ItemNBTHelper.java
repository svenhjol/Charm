package svenhjol.charm.base.helper;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

@SuppressWarnings("unused")
public class ItemNBTHelper {
    public static int getInt(ItemStack stack, String tag, int defaultExpected) {
        return tagExists(stack, tag) ? getNBT(stack).getInt(tag) : defaultExpected;
    }

    public static boolean getBoolean(ItemStack stack, String tag, boolean defaultExpected) {
        return tagExists(stack, tag) ? getNBT(stack).getBoolean(tag) : defaultExpected;
    }

    public static double getDouble(ItemStack stack, String tag, double defaultExpected) {
        return tagExists(stack, tag) ? getNBT(stack).getDouble(tag) : defaultExpected;
    }

    public static long getLong(ItemStack stack, String tag, long defaultExpected) {
        return tagExists(stack, tag) ? getNBT(stack).getLong(tag) : defaultExpected;
    }

    public static String getString(ItemStack stack, String tag, String defaultExpected) {
        return tagExists(stack, tag) ? getNBT(stack).getString(tag) : defaultExpected;
    }

    public static CompoundTag getCompound(ItemStack stack, String tag) {
        return getCompound(stack, tag, false);
    }

    public static CompoundTag getCompound(ItemStack stack, String tag, boolean nullify) {
        return tagExists(stack, tag) ? getNBT(stack).getCompound(tag) : (nullify ? null : new CompoundTag());
    }

    public static void setInt(ItemStack stack, String tag, int i) {
        getNBT(stack).putInt(tag, i);
    }

    public static void setBoolean(ItemStack stack, String tag, boolean b) {
        getNBT(stack).putBoolean(tag, b);
    }

    public static void setCompound(ItemStack stack, String tag, CompoundTag cmp) {
        getNBT(stack).put(tag, cmp);
    }

    public static void setDouble(ItemStack stack, String tag, double d) {
        getNBT(stack).putDouble(tag, d);
    }

    public static void setLong(ItemStack stack, String tag, long l) {
        getNBT(stack).putLong(tag, l);
    }

    public static void setString(ItemStack stack, String tag, String s) {
        getNBT(stack).putString(tag, s);
    }


    public static boolean tagExists(ItemStack stack, String tag) {
        return !stack.isEmpty() && stack.hasTag() && getNBT(stack).contains(tag);
    }

    public static CompoundTag getNBT(ItemStack stack) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        return stack.getTag();
    }
}
