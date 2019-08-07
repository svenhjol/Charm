package svenhjol.meson.helper;

import net.minecraft.block.ComposterBlock;
import net.minecraft.item.ItemStack;

public class ComposterHelper
{
    public static void addCompostableItem(ItemStack stack, float chance)
    {
        ComposterBlock.CHANCES.put(stack.getItem(), chance);
    }
}
