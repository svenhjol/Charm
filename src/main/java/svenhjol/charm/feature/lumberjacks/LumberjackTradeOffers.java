package svenhjol.charm.feature.lumberjacks;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import svenhjol.charmony.helper.GenericTradeOffers;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LumberjackTradeOffers {
    public static class SaplingsForEmeralds implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int baseEmeralds;
        private final int extraEmeralds;
        private final int maxUses;
        private final List<Item> saplings;

        public SaplingsForEmeralds(List<Item> saplings, int baseEmeralds, int extraEmeralds, int villagerXp, int maxUses) {
            this.baseEmeralds = baseEmeralds;
            this.extraEmeralds = extraEmeralds;
            this.villagerXp = villagerXp;
            this.maxUses = maxUses;
            this.saplings = saplings;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            var sapling = new ItemStack(saplings.get(random.nextInt(saplings.size())));

            return new MerchantOffer(
                GenericTradeOffers.getStack(random, Items.EMERALD, baseEmeralds, extraEmeralds),
                sapling,
                maxUses,
                villagerXp,
                0.2F
            );
        }
    }
    public static class BarkForLogs implements VillagerTrades.ItemListing {
        private final int villagerXp;
        private final int baseCost;
        private final int extraCost;
        private final int maxUses;

        @SuppressWarnings("unused")
        public BarkForLogs(int baseCost, int villagerXp, int maxUses) {
            this(baseCost, 0, villagerXp, maxUses);
        }

        public BarkForLogs(int baseCost, int extraCost, int villagerXp, int maxUses) {
            this.baseCost = baseCost;
            this.extraCost = extraCost;
            this.villagerXp = villagerXp;
            this.maxUses = maxUses;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity merchant, RandomSource random) {
            Map<Block, Block> map = new HashMap<>();

            map.put(Blocks.ACACIA_LOG, Blocks.ACACIA_WOOD);
            map.put(Blocks.BIRCH_LOG, Blocks.BIRCH_WOOD);
            map.put(Blocks.DARK_OAK_LOG, Blocks.DARK_OAK_WOOD);
            map.put(Blocks.JUNGLE_LOG, Blocks.JUNGLE_WOOD);
            map.put(Blocks.MANGROVE_LOG, Blocks.MANGROVE_WOOD);
            map.put(Blocks.OAK_LOG, Blocks.OAK_WOOD);
            map.put(Blocks.SPRUCE_LOG, Blocks.SPRUCE_WOOD);

            var index = random.nextInt(map.size());
            var logs = new ArrayList<>(map.keySet());

            var log = logs.get(index);
            var wood = map.get(log);
            var cost = baseCost + random.nextInt(extraCost + 1);

            return new MerchantOffer(
                new ItemStack(log, cost),
                new ItemStack(wood, cost),
                maxUses,
                villagerXp,
                0.2F
            );
        }
    }
}
