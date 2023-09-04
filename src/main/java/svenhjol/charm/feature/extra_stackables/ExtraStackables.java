package svenhjol.charm.feature.extra_stackables;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.Charm;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmFeature;
import svenhjol.charm.mixin.accessor.ItemAccessor;

@Feature(mod = Charm.MOD_ID, description = "Allows some unstackable items to stack.")
public class ExtraStackables extends CharmFeature {
    @Configurable(name = "Enchanted book", description = "Enchanted book maximum stack size.")
    public static int enchantedBookStackSize = 16;

    @Configurable(name = "Potion", description = "Potion maximum stack size.")
    public static int potionStackSize = 16;

    @Configurable(name = "Splash potion", description = "Splash potion maximum stack size.")
    public static int splashPotionStackSize = 1;

    @Configurable(name = "Lingering potion", description = "Lingering potion maximum stack size.")
    public static int lingeringPotionStackSize = 1;

    @Configurable(name = "Stew", description = "Stew (and beetroot soup) maximum stack size.")
    public static int stewStackSize = 16;

    @Configurable(name = "Suspicious stew", description = "Suspicious stew maximum stack size.")
    public static int suspiciousStewStackSize = 1;

    @Override
    public void register() {
        if (enchantedBookStackSize > 1) {
            ((ItemAccessor)Items.ENCHANTED_BOOK).setMaxStackSize(enchantedBookStackSize);
        }
        if (potionStackSize > 1) {
            ((ItemAccessor)Items.POTION).setMaxStackSize(potionStackSize);
        }
        if (splashPotionStackSize > 1) {
            ((ItemAccessor)Items.SPLASH_POTION).setMaxStackSize(splashPotionStackSize);
        }
        if (lingeringPotionStackSize > 1) {
            ((ItemAccessor)Items.LINGERING_POTION).setMaxStackSize(lingeringPotionStackSize);
        }
        if (stewStackSize > 1) {
            ((ItemAccessor)Items.MUSHROOM_STEW).setMaxStackSize(stewStackSize);
            ((ItemAccessor)Items.RABBIT_STEW).setMaxStackSize(stewStackSize);
            ((ItemAccessor)Items.BEETROOT_SOUP).setMaxStackSize(stewStackSize);
        }
        if (suspiciousStewStackSize > 1) {
            ((ItemAccessor)Items.SUSPICIOUS_STEW).setMaxStackSize(suspiciousStewStackSize);
        }
    }

    public static ItemStack getReducedStack(ItemStack stack) {
        if (stack.getItem() == Items.ENCHANTED_BOOK) {
            stack.shrink(1);
            return stack;
        }
        return ItemStack.EMPTY;
    }
}
