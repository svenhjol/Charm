package svenhjol.meson.helper;

import net.minecraft.util.text.TextFormatting;

import java.util.HashMap;
import java.util.Map;

public class TextHelper
{
    public static Map<Integer, Integer> dyeTextMap = new HashMap<>();

    static {
        dyeTextMap.put(0, 15); // black -> white
        dyeTextMap.put(1, 12); // red
        dyeTextMap.put(2, 2); // green -> dak_green
        dyeTextMap.put(3, 4); // brown -> dark_red
        dyeTextMap.put(4, 1); // blue -> dark_blue
        dyeTextMap.put(5, 5); // purple -> dark_purple
        dyeTextMap.put(6, 11); // cyan -> aqua
        dyeTextMap.put(7, 7); // silver -> gray
        dyeTextMap.put(8, 8); // gray -> dark_gray
        dyeTextMap.put(9, 13); // pink -> light_purple
        dyeTextMap.put(10, 10); // lime -> green
        dyeTextMap.put(11, 14); // yellow -> yellow
        dyeTextMap.put(12, 9); // light_blue -> blue
        dyeTextMap.put(13, 13); // magenta - light_purple
        dyeTextMap.put(14, 6); // orange -> gold
        dyeTextMap.put(15, 15); // white
    }

    public static TextFormatting getTextFormattingByDyeDamage(int dyeDamage)
    {
        return TextFormatting.fromColorIndex(dyeTextMap.get(dyeDamage));
    }
}
