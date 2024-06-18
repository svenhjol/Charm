package svenhjol.charm.feature.item_stacking;

import net.minecraft.util.Mth;
import svenhjol.charm.feature.atlases.common.Item;
import svenhjol.charm.feature.item_stacking.common.Registers;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "Allows some unstackable items to stack.")
public final class ItemStacking extends CommonFeature {
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
    private static int stewStackSize = 64;

    @Configurable(name = "Suspicious stew", description = "Suspicious stew maximum stack size.")
    private static int suspiciousStewStackSize = 1;

    public ItemStacking(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
    }

    public int enchantedBookStackSize() {
        return Mth.clamp(enchantedBookStackSize, 1, Item.MAX_STACK_SIZE);
    }

    public int potionStackSize() {
        return Mth.clamp(potionStackSize, 1, Item.MAX_STACK_SIZE);
    }

    public int splashPotionStackSize() {
        return Mth.clamp(splashPotionStackSize, 1, Item.MAX_STACK_SIZE);
    }

    public int lingeringPotionStackSize() {
        return Mth.clamp(lingeringPotionStackSize, 1, Item.MAX_STACK_SIZE);
    }

    public int stewStackSize() {
        return Mth.clamp(stewStackSize, 1, Item.MAX_STACK_SIZE);
    }

    public int suspiciousStewStackSize() {
        return Mth.clamp(suspiciousStewStackSize, 1, Item.MAX_STACK_SIZE);
    }
}
