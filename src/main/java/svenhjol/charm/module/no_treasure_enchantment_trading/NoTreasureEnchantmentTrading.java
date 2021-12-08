package svenhjol.charm.module.no_treasure_enchantment_trading;

import net.minecraft.world.item.enchantment.Enchantment;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.loader.CharmModule;

@CommonModule(mod = Charm.MOD_ID, enabledByDefault = false, description = "Treasure enchantments such as Mending and Soul Speed are no longer tradeable with villagers.\n" +
    "This is an opinionated feature designed to force the player to explore and so is disabled by default.")
public class NoTreasureEnchantmentTrading extends CharmModule {
    public static boolean isTradeable(Enchantment enchantment) {
        return enchantment.isTradeable() && !enchantment.isTreasureOnly();
    }
}
