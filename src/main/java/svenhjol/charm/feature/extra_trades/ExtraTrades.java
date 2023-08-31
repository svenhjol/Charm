package svenhjol.charm.feature.extra_trades;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;
import svenhjol.charm.Charm;
import svenhjol.charm_api.CharmApi;
import svenhjol.charm_api.iface.IProvidesWandererTrades;
import svenhjol.charm_api.iface.IWandererTrade;
import svenhjol.charm_core.annotation.Configurable;
import svenhjol.charm_core.annotation.Feature;
import svenhjol.charm_core.base.CharmFeature;
import svenhjol.charm_core.helper.GenericTradeOffers;

import javax.annotation.Nullable;
import java.util.List;

@Feature(mod = Charm.MOD_ID, description = "Adds more villager trades.")
public class ExtraTrades extends CharmFeature implements IProvidesWandererTrades {
    @Configurable(name = "Enchanted books", description = "If true, librarians will buy any enchanted book in return for emeralds.")
    public static boolean enchantedBooks = true;

    @Configurable(name = "Repaired anvils", description = "If true, armorers, weaponsmiths and toolsmiths will buy chipped or damaged anvils along with iron ingots in return for repaired anvils.")
    public static boolean repairedAnvils = true;

    @Configurable(name = "Leather for rotten flesh", description = "If true, leatherworkers will sell leather in return for rotten flesh.")
    public static boolean leatherForRottenFlesh = true;

    @Configurable(name = "Beef for rotten flesh", description = "If true, butchers will sell beef in return for rotten flesh.")
    public static boolean beefForRottenFlesh = true;

    @Configurable(name = "Bundles", description = "If true, leatherworkers will sell bundles in return for emeralds.")
    public static boolean bundles = true;

    @Configurable(name = "Phantom membrane", description = "If true, clerics and wandering traders will sell phantom membrane in return for emeralds.")
    public static boolean phantomMembrane = true;

    @Override
    public void register() {
        CharmApi.registerProvider(this);
        var registry = Charm.instance().registry();

        if (enchantedBooks) {
            var tier = 2;
            var xp = 5;
            registry.villagerTrade(() -> VillagerProfession.LIBRARIAN, tier, () -> new GenericTradeOffers.EmeraldsForItems(
                Items.ENCHANTED_BOOK, 1, 0, 5, 0, xp, 16));
        }

        if (repairedAnvils) {
            var tier = 2;
            var xp = 5;
            registry.villagerTrade(() -> VillagerProfession.ARMORER, tier, () -> new AnvilRepair(tier, xp));
            registry.villagerTrade(() -> VillagerProfession.WEAPONSMITH, tier, () -> new AnvilRepair(tier, xp));
            registry.villagerTrade(() -> VillagerProfession.TOOLSMITH, tier, () -> new AnvilRepair(tier, xp));
        }

        if (leatherForRottenFlesh) {
            var tier = 3;
            var xp = 10;
            registry.villagerTrade(() -> VillagerProfession.LEATHERWORKER, tier, () -> new GenericTradeOffers.ItemsForItems(
                Items.ROTTEN_FLESH, Items.LEATHER, 10, 5, 1, 0, xp, 8));
        }

        if (beefForRottenFlesh) {
            var tier = 3;
            var xp = 10;
            registry.villagerTrade(() -> VillagerProfession.BUTCHER, tier, () -> new GenericTradeOffers.ItemsForItems(
                Items.ROTTEN_FLESH, Items.BEEF, 8, 5, 1, 0, xp, 8));
        }

        if (phantomMembrane) {
            var tier = 4;
            var xp = 15;
            registry.villagerTrade(() -> VillagerProfession.CLERIC, tier, () -> new GenericTradeOffers.EmeraldsForItems(
                Items.PHANTOM_MEMBRANE, 3, 3, 1, 0, xp, 8));
        }

        if (bundles) {
            var tier = 5;
            var xp = 30;
            registry.villagerTrade(() -> VillagerProfession.LEATHERWORKER, tier, () -> new GenericTradeOffers.EmeraldsForItems(
                Items.BUNDLE, 12, 10, 1, 0, xp, 1));
        }
    }

    @Override
    public List<IWandererTrade> getWandererTrades() {
        return List.of();
    }

    @Override
    public List<IWandererTrade> getRareWandererTrades() {
        return phantomMembrane ? List.of(
            new IWandererTrade() {
                @Override
                public ItemLike getItem() {
                    return Items.PHANTOM_MEMBRANE;
                }

                @Override
                public int getCount() {
                    return 5;
                }

                @Override
                public int getCost() {
                    return 10;
                }
            }
        ) : List.of();
    }

    static class AnvilRepair implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int maxUses;

        public AnvilRepair(int villagerXp, int maxUses) {
            this.villagerXp = villagerXp;
            this.maxUses = maxUses;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, RandomSource random) {
            var item = random.nextBoolean() ? Items.DAMAGED_ANVIL : Items.CHIPPED_ANVIL;

            return new MerchantOffer(
                new ItemStack(item),
                GenericTradeOffers.getStack(random, Items.IRON_INGOT, 5, 4),
                new ItemStack(Items.ANVIL),
                maxUses,
                villagerXp,
                0.2F
            );
        }
    }
}
