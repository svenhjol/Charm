package svenhjol.charm.feature.extra_stackables;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.common.CommonFeature;

public class ExtraStackables extends CommonFeature {
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
    public String description() {
        return "Allows some unstackable items to stack.";
    }

    @Override
    public void register() {
        if (enchantedBookStackSize > 1) {
            Items.ENCHANTED_BOOK.maxStackSize = enchantedBookStackSize;
        }
        if (potionStackSize > 1) {
            Items.POTION.maxStackSize = potionStackSize;
        }
        if (splashPotionStackSize > 1) {
            Items.SPLASH_POTION.maxStackSize = splashPotionStackSize;
        }
        if (lingeringPotionStackSize > 1) {
            Items.LINGERING_POTION.maxStackSize = lingeringPotionStackSize;
        }
        if (stewStackSize > 1) {
            Items.MUSHROOM_STEW.maxStackSize = stewStackSize;
            Items.RABBIT_STEW.maxStackSize = stewStackSize;
            Items.BEETROOT_SOUP.maxStackSize = stewStackSize;
        }
        if (suspiciousStewStackSize > 1) {
            Items.SUSPICIOUS_STEW.maxStackSize = suspiciousStewStackSize;
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
