package svenhjol.charm.base.compat;

import net.minecraft.item.ItemStack;
import net.minecraftforge.event.AnvilUpdateEvent;
import svenhjol.meson.enums.ColorVariant;

public interface IQuarkCompat {
    void onTallowAnvilUpdate(AnvilUpdateEvent event);
    boolean hasColorRuneModule();
    boolean isRune(ItemStack stack);
    ColorVariant getRuneColor(ItemStack stack);
    ItemStack getRune(ColorVariant color);
}
