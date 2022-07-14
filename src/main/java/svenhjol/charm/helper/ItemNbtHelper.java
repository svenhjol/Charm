package svenhjol.charm.helper;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @version 4.0.0-charm
 */
@SuppressWarnings("unused")
public class ItemNbtHelper {
    public static int getInt(ItemStack stack, String nbt, int fallback) {
        return tagExists(stack, nbt) ? getTag(stack).getInt(nbt) : fallback;
    }

    public static boolean getBoolean(ItemStack stack, String nbt, boolean fallback) {
        return tagExists(stack, nbt) ? getTag(stack).getBoolean(nbt) : fallback;
    }

    public static double getDouble(ItemStack stack, String nbt, double fallback) {
        return tagExists(stack, nbt) ? getTag(stack).getDouble(nbt) : fallback;
    }

    public static long getLong(ItemStack stack, String nbt, long fallback) {
        return tagExists(stack, nbt) ? getTag(stack).getLong(nbt) : fallback;
    }

    public static String getString(ItemStack stack, String nbt, String fallback) {
        return tagExists(stack, nbt) ? getTag(stack).getString(nbt) : fallback;
    }

    public static CompoundTag getCompound(ItemStack stack, String tag) {
        return getCompound(stack, tag, false);
    }

    @Nullable
    public static CompoundTag getCompound(ItemStack stack, String tag, boolean nullable) {
        return tagExists(stack, tag) ? getTag(stack).getCompound(tag) : (nullable ? null : new CompoundTag());
    }

    @Nullable
    public static UUID getUuid(ItemStack stack, String tag) {
        CompoundTag t = getTag(stack);
        return t.hasUUID(tag) ? t.getUUID(tag) : null;
    }

    public static ListTag getList(ItemStack stack, String nbt) {
        return tagExists(stack, nbt) ? getTag(stack).getList(nbt, 10) : new ListTag();
    }

    public static void setInt(ItemStack stack, String nbt, int i) {
        getTag(stack).putInt(nbt, i);
    }

    public static void setBoolean(ItemStack stack, String nbt, boolean b) {
        getTag(stack).putBoolean(nbt, b);
    }

    public static void setCompound(ItemStack stack, String nbt, CompoundTag tag) {
        getTag(stack).put(nbt, tag);
    }

    public static void setDouble(ItemStack stack, String nbt, double d) {
        getTag(stack).putDouble(nbt, d);
    }

    public static void setLong(ItemStack stack, String nbt, long l) {
        getTag(stack).putLong(nbt, l);
    }

    public static void setString(ItemStack stack, String nbt, String s) {
        getTag(stack).putString(nbt, s);
    }

    public static void setUuid(ItemStack stack, String nbt, UUID uuid) {
        getTag(stack).putUUID(nbt, uuid);
    }

    public static void setList(ItemStack stack, String nbt, ListTag list) {
        getTag(stack).put(nbt, list);
    }

    public static boolean tagExists(ItemStack stack, String nbt) {
        return !stack.isEmpty() && stack.hasTag() && getTag(stack).contains(nbt);
    }

    public static CompoundTag getTag(ItemStack stack) {
        if (!stack.hasTag()) {
            stack.setTag(new CompoundTag());
        }
        return stack.getTag();
    }
}
