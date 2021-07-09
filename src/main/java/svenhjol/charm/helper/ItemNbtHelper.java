package svenhjol.charm.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import java.util.UUID;

/**
 * @version 1.0.0-charm
 */
@SuppressWarnings("unused")
public class ItemNbtHelper {
    public static int getInt(ItemStack stack, String nbt, int defaultExpected) {
        return nbtExists(stack, nbt) ? getNBT(stack).getInt(nbt) : defaultExpected;
    }

    public static boolean getBoolean(ItemStack stack, String nbt, boolean defaultExpected) {
        return nbtExists(stack, nbt) ? getNBT(stack).getBoolean(nbt) : defaultExpected;
    }

    public static double getDouble(ItemStack stack, String nbt, double defaultExpected) {
        return nbtExists(stack, nbt) ? getNBT(stack).getDouble(nbt) : defaultExpected;
    }

    public static long getLong(ItemStack stack, String nbt, long defaultExpected) {
        return nbtExists(stack, nbt) ? getNBT(stack).getLong(nbt) : defaultExpected;
    }

    public static String getString(ItemStack stack, String nbt, String defaultExpected) {
        return nbtExists(stack, nbt) ? getNBT(stack).getString(nbt) : defaultExpected;
    }

    public static CompoundTag getCompound(ItemStack stack, String nbt) {
        return getCompound(stack, nbt, false);
    }

    public static CompoundTag getCompound(ItemStack stack, String nbt, boolean nullify) {
        return nbtExists(stack, nbt) ? getNBT(stack).getCompound(nbt) : (nullify ? null : new CompoundTag());
    }

    public static UUID getUuid(ItemStack stack, String nbtName) {
        CompoundTag nbt = getNBT(stack);
        return nbt.hasUUID(nbtName) ? nbt.getUUID(nbtName) : null;
    }

    public static ListTag getList(ItemStack stack, String nbt) {
        return nbtExists(stack, nbt) ? getNBT(stack).getList(nbt, 10) : new ListTag();
    }

    public static void setInt(ItemStack stack, String nbt, int i) {
        getNBT(stack).putInt(nbt, i);
    }

    public static void setBoolean(ItemStack stack, String nbt, boolean b) {
        getNBT(stack).putBoolean(nbt, b);
    }

    public static void setCompound(ItemStack stack, String nbt, CompoundTag tag) {
        getNBT(stack).put(nbt, tag);
    }

    public static void setDouble(ItemStack stack, String nbt, double d) {
        getNBT(stack).putDouble(nbt, d);
    }

    public static void setLong(ItemStack stack, String nbt, long l) {
        getNBT(stack).putLong(nbt, l);
    }

    public static void setString(ItemStack stack, String nbt, String s) {
        getNBT(stack).putString(nbt, s);
    }

    public static void setUuid(ItemStack stack, String nbt, UUID uuid) {
        getNBT(stack).putUUID(nbt, uuid);
    }

    public static void setList(ItemStack stack, String nbt, ListTag list) {
        getNBT(stack).put(nbt, list);
    }


    public static boolean nbtExists(ItemStack stack, String nbt) {
        return !stack.isEmpty() && stack.hasTag() && getNBT(stack).contains(nbt);
    }

    public static CompoundTag getNBT(ItemStack stack) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        return stack.getTag();
    }
}
