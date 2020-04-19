package svenhjol.charm.base.compat;

import net.minecraft.item.ItemStack;
import svenhjol.meson.enums.ColorVariant;

public interface IQuarkCompat {
    boolean hasColorRuneModule();
    boolean isRune(ItemStack stack);
    ColorVariant getRuneColor(ItemStack stack);
    ItemStack getRune(ColorVariant color);
    ItemStack getQuiltedWool(ColorVariant color);
}
