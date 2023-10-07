package svenhjol.charm.feature.extra_trades;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import svenhjol.charm.Charm;
import svenhjol.charmony_api.CharmonyApi;
import svenhjol.charmony_api.iface.IWandererTradeProvider;
import svenhjol.charmony.annotation.Configurable;
import svenhjol.charmony.annotation.Feature;
import svenhjol.charmony.base.CharmonyFeature;
import svenhjol.charmony.helper.ApiHelper;
import svenhjol.charmony.helper.GenericTradeOffers;

import javax.annotation.Nullable;

@Feature(mod = Charm.MOD_ID, description = "Adds more villager trades.")
public class ExtraTrades extends CharmonyFeature implements IWandererTradeProvider {
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

    @Configurable(name = "Phantom membrane", description = "If true, clerics will sell phantom membrane in return for emeralds.")
    public static boolean phantomMembrane = true;

    @Configurable(name = "Charm mod items", description = "If true, wandering traders have a chance to sell various items from Charm mods.")
    public static boolean charmModItems = true;

    @Override
    public void register() {
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

        if (charmModItems) {
            ApiHelper.consume(IWandererTradeProvider.class,
                provider -> {
                    provider.getWandererTrades().forEach(
                        trade -> registry.wandererTrade(
                            () -> new GenericTradeOffers.ItemsForEmeralds(trade.getItem(), trade.getCost(), trade.getCount(), 0, 1), false));

                    provider.getRareWandererTrades().forEach(
                        trade -> registry.wandererTrade(
                            () -> new GenericTradeOffers.ItemsForEmeralds(trade.getItem(), trade.getCost(), trade.getCount(), 0, 1), true));
                });
        }

        CharmonyApi.registerProvider(this);
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
