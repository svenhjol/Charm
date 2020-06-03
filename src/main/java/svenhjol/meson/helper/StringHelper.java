package svenhjol.meson.helper;

import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

@SuppressWarnings("unused")
public class StringHelper {
    public static Map<Integer, Integer> dyeTextMap = new HashMap<>();

    static {
        dyeTextMap.put(15, 15);
        dyeTextMap.put(14, 12);
        dyeTextMap.put(13, 2);
        dyeTextMap.put(12, 4);
        dyeTextMap.put(11, 1);
        dyeTextMap.put(10, 5);
        dyeTextMap.put(9, 11);
        dyeTextMap.put(8, 7);
        dyeTextMap.put(7, 8);
        dyeTextMap.put(6, 13);
        dyeTextMap.put(5, 10);
        dyeTextMap.put(4, 14);
        dyeTextMap.put(3, 9);
        dyeTextMap.put(2, 13);
        dyeTextMap.put(1, 6);
        dyeTextMap.put(0, 15);
    }

    public static String formatBlockPos(BlockPos pos) {
        if (pos == null) return "";
        return pos.getX() + " " + pos.getY() + " " + pos.getZ();
    }


    public static TextFormatting getTextFormattingByDyeDamage(int dyeDamage) {
        return TextFormatting.fromColorIndex(dyeTextMap.get(dyeDamage));
    }

    // see https://stackoverflow.com/a/1892812
    public static String capitalizeString(String string) {
        char[] chars = string.toLowerCase().toCharArray();
        boolean found = false;
        for (int i = 0; i < chars.length; i++) {
            if (!found && Character.isLetter(chars[i])) {
                chars[i] = Character.toUpperCase(chars[i]);
                found = true;
            } else if (Character.isWhitespace(chars[i]) || chars[i]=='.' || chars[i]=='\'') { // You can add other chars here
                found = false;
            }
        }
        return String.valueOf(chars);
    }
}
