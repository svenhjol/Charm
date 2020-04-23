package svenhjol.charm.base.compat;

import net.minecraft.item.DyeColor;
import net.minecraft.item.ItemStack;
import svenhjol.meson.enums.ColorVariant;

public interface IQuarkCompat {
    boolean hasColorRuneModule();
    boolean isRune(ItemStack stack);
    ColorVariant getRuneColor(ItemStack stack);
    ItemStack getRune(ColorVariant color);
    ItemStack getQuiltedWool(ColorVariant color);
    void applyColor(ItemStack stack, DyeColor color);
}
