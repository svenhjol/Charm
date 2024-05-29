package svenhjol.charm.feature.trade_improvements;

import svenhjol.charm.feature.trade_improvements.common.Providers;
import svenhjol.charm.feature.trade_improvements.common.Registers;
import svenhjol.charm.charmony.annotation.Configurable;
import svenhjol.charm.charmony.annotation.Feature;
import svenhjol.charm.charmony.common.CommonFeature;
import svenhjol.charm.charmony.common.CommonLoader;

@Feature(description = "Adds more villager trades.")
public final class TradeImprovements extends CommonFeature {
    public final Registers registers;
    public final Providers providers;

    @Configurable(name = "Enchanted books", description = "If true, librarians will buy any enchanted book in return for emeralds.")
    private static boolean enchantedBooks = true;

    @Configurable(name = "Repaired anvils", description = "If true, armorers, weaponsmiths and toolsmiths will buy chipped or damaged anvils along with iron ingots in return for repaired anvils.")
    private static boolean repairedAnvils = true;

    @Configurable(name = "Leather for rotten flesh", description = "If true, leatherworkers will sell leather in return for rotten flesh.")
    private static boolean leatherForRottenFlesh = true;

    @Configurable(name = "Beef for rotten flesh", description = "If true, butchers will sell beef in return for rotten flesh.")
    private static boolean beefForRottenFlesh = true;

    @Configurable(name = "Bundles", description = "If true, leatherworkers will sell bundles in return for emeralds.")
    private static boolean bundles = true;

    @Configurable(name = "Phantom membrane", description = "If true, clerics will sell phantom membrane in return for emeralds.")
    private static boolean phantomMembrane = true;

    @Configurable(name = "Charm mod items", description = "If true, wandering traders have a chance to sell various items from Charm mods.")
    private static boolean charmModItems = true;

    public TradeImprovements(CommonLoader loader) {
        super(loader);

        registers = new Registers(this);
        providers = new Providers(this);
    }

    public boolean enchantedBooks() {
        return isEnabled() && enchantedBooks;
    }

    public boolean repairedAnvils() {
        return isEnabled() && repairedAnvils;
    }

    public boolean leatherForRottenFlesh() {
        return isEnabled() && leatherForRottenFlesh;
    }

    public boolean beefForRottenFlesh() {
        return isEnabled() && beefForRottenFlesh;
    }

    public boolean bundles() {
        return isEnabled() && bundles;
    }

    public boolean phantomMembrane() {
        return isEnabled() && phantomMembrane;
    }

    public boolean charmModItems() {
        return isEnabled() && charmModItems;
    }
}
