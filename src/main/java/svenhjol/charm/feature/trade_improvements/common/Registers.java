package svenhjol.charm.feature.trade_improvements.common;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import svenhjol.charm.feature.trade_improvements.TradeImprovements;
import svenhjol.charm.charmony.feature.RegisterHolder;
import svenhjol.charm.charmony.common.villages.GenericTrades;

import javax.annotation.Nullable;
import java.util.Optional;

public final class Registers extends RegisterHolder<TradeImprovements> {
    public Registers(TradeImprovements feature) {
        super(feature);
    }

    @Override
    public void onEnabled() {
        var registry = feature().registry();

        if (feature().enchantedBooks()) {
            var tier = 2;
            var xp = 5;
            registry.villagerTrade(() -> VillagerProfession.LIBRARIAN, tier, () -> new GenericTrades.EmeraldsForItems(
                Items.ENCHANTED_BOOK, 1, 0, 5, 0, xp, 16));
        }

        if (feature().repairedAnvils()) {
            var tier = 2;
            var xp = 5;
            registry.villagerTrade(() -> VillagerProfession.ARMORER, tier, () -> new AnvilRepair(tier, xp));
            registry.villagerTrade(() -> VillagerProfession.WEAPONSMITH, tier, () -> new AnvilRepair(tier, xp));
            registry.villagerTrade(() -> VillagerProfession.TOOLSMITH, tier, () -> new AnvilRepair(tier, xp));
        }

        if (feature().leatherForRottenFlesh()) {
            var tier = 3;
            var xp = 10;
            registry.villagerTrade(() -> VillagerProfession.LEATHERWORKER, tier, () -> new GenericTrades.ItemsForItems(
                Items.ROTTEN_FLESH, Items.LEATHER, 10, 5, 1, 0, xp, 8));
        }

        if (feature().beefForRottenFlesh()) {
            var tier = 3;
            var xp = 10;
            registry.villagerTrade(() -> VillagerProfession.BUTCHER, tier, () -> new GenericTrades.ItemsForItems(
                Items.ROTTEN_FLESH, Items.BEEF, 8, 5, 1, 0, xp, 8));
        }

        if (feature().phantomMembrane()) {
            var tier = 4;
            var xp = 15;
            registry.villagerTrade(() -> VillagerProfession.CLERIC, tier, () -> new GenericTrades.EmeraldsForItems(
                Items.PHANTOM_MEMBRANE, 3, 3, 1, 0, xp, 8));
        }

        if (feature().bundles()) {
            var tier = 5;
            var xp = 30;
            registry.villagerTrade(() -> VillagerProfession.LEATHERWORKER, tier, () -> new GenericTrades.EmeraldsForItems(
                Items.BUNDLE, 12, 10, 1, 0, xp, 1));
        }
    }

    public static class AnvilRepair implements VillagerTrades.ItemListing {
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
                GenericTrades.getCost(item, 1),
                Optional.of(GenericTrades.getCost(random, Items.IRON_INGOT, 5, 4)),
                new ItemStack(Items.ANVIL),
                maxUses,
                villagerXp,
                0.2f
            );
        }
    }
}
