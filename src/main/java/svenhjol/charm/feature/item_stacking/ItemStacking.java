package svenhjol.charm.feature.item_stacking;

import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import svenhjol.charm.feature.atlases.common.Item;
import svenhjol.charm.feature.item_stacking.common.Registers;
import svenhjol.charm.foundation.annotation.Configurable;
import svenhjol.charm.foundation.annotation.Feature;
import svenhjol.charm.foundation.common.CommonFeature;
import svenhjol.charm.foundation.common.CommonLoader;

@Feature(description = "Allows some unstackable items to stack.")
public class ItemStacking extends CommonFeature {
    public final Registers registers;

    @Configurable(name = "Enchanted book", description = "Enchanted book maximum stack size.")
    private static int enchantedBookStackSize = 16;

    @Configurable(name = "Potion", description = "Potion maximum stack size.")
    private static int potionStackSize = 16;

    @Configurable(name = "Splash potion", description = "Splash potion maximum stack size.")
    private static int splashPotionStackSize = 1;

    @Configurable(name = "Lingering potion", description = "Lingering potion maximum stack size.")
    private static int lingeringPotionStackSize = 1;

    @Configurable(name = "Stew", description = "Stew (and beetroot soup) maximum stack size.")
    private static int stewStackSize = 16;

    @Configurable(name = "Suspicious stew", description = "Suspicious stew maximum stack size.")
    private static int suspiciousStewStackSize = 1;

    public ItemStacking(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    public int enchantedBookStackSize() {
        return Mth.clamp(enchantedBookStackSize, 1, Item.DEFAULT_MAX_STACK_SIZE);
    }

    public int potionStackSize() {
        return Mth.clamp(potionStackSize, 1, Item.DEFAULT_MAX_STACK_SIZE);
    }

    public int splashPotionStackSize() {
        return Mth.clamp(splashPotionStackSize, 1, Item.DEFAULT_MAX_STACK_SIZE);
    }

    public int lingeringPotionStackSize() {
        return Mth.clamp(lingeringPotionStackSize, 1, Item.DEFAULT_MAX_STACK_SIZE);
    }

    public int stewStackSize() {
        return Mth.clamp(stewStackSize, 1, Item.DEFAULT_MAX_STACK_SIZE);
    }

    public int suspiciousStewStackSize() {
        return Mth.clamp(suspiciousStewStackSize, 1, Item.DEFAULT_MAX_STACK_SIZE);
    }

    public static ItemStack getReducedStack(ItemStack stack) {
        if (stack.getItem() == Items.ENCHANTED_BOOK) {
            stack.shrink(1);
            return stack;
        }
        return ItemStack.EMPTY;
    }
}
