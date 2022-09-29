package svenhjol.charm.helper;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.core.NonNullList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerProfession;
import net.minecraft.world.entity.npc.VillagerTrades.ItemListing;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.ItemLike;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static net.minecraft.world.entity.npc.VillagerTrades.TRADES;
import static net.minecraft.world.entity.npc.VillagerTrades.WANDERING_TRADER_TRADES;

/**
 * @version 4.0.0-charm
 */
public class VillagerHelper {
    public static void removeTrade(VillagerProfession profession, int level, Predicate<ItemListing> match) {
        var trades = getMutableTrades(profession);
        trades.get(level).stream()
            .filter(match)
            .findFirst()
            .ifPresent(trade -> {
                LogHelper.debug(VillagerHelper.class, "Removing trade. Profession: " + profession.name() + ", level: " + level + ", trade: " + trade);
                trades.get(level).remove(trade);
                reassembleTrades(profession, trades);
            });
    }

    public static void addTrade(VillagerProfession profession, int level, ItemListing trade) {
        var trades = getMutableTrades(profession);
        trades.get(level).add(trade);
        reassembleTrades(profession, trades);
    }

    public static void addWanderingTrade(ItemListing trade, boolean isRare) {
        if (isRare) {
            List<ItemListing> rareTrades = NonNullList.create();
            rareTrades.addAll(Arrays.asList(WANDERING_TRADER_TRADES.get(2)));
            rareTrades.add(trade);
            WANDERING_TRADER_TRADES.put(2, rareTrades.toArray(new ItemListing[0]));
        } else {
            List<ItemListing> normalTrades = NonNullList.create();
            normalTrades.addAll(Arrays.asList(WANDERING_TRADER_TRADES.get(1)));
            normalTrades.add(trade);
            WANDERING_TRADER_TRADES.put(1, normalTrades.toArray(new ItemListing[0]));
        }
    }

    private static Int2ObjectMap<List<ItemListing>> getMutableTrades(VillagerProfession profession) {
        var fixedTrades = TRADES.get(profession);
        Int2ObjectMap<List<ItemListing>> mutableTrades = new Int2ObjectOpenHashMap<>();

        for (int i = 1; i <= 5; i++) {
            mutableTrades.put(i, NonNullList.create());
        }

        fixedTrades.int2ObjectEntrySet().forEach(e
            -> Arrays.stream(e.getValue())
                .forEach(a -> mutableTrades.get(e.getIntKey()).add(a)));

        return mutableTrades;
    }

    private static void reassembleTrades(VillagerProfession profession, Int2ObjectMap<List<ItemListing>> trades) {
        Int2ObjectMap<ItemListing[]> mappedTrades = new Int2ObjectOpenHashMap<>();
        trades.int2ObjectEntrySet().forEach(e
            -> mappedTrades.put(e.getIntKey(), e.getValue().toArray(new ItemListing[0])));

        TRADES.put(profession, mappedTrades);
    }

    public static class SingleItemTypeTrade implements ItemListing {
        protected ItemLike in = Items.AIR;
        protected ItemLike out = Items.EMERALD;
        protected int inCount = 1;
        protected int outCount = 1;
        protected int maxUses = 20;
        protected int experience = 2;
        protected float multiplier = 0.05F;

        public void setInput(ItemLike item, int count) {
            this.in = item;
            this.inCount = count;
        }

        public void setOutput(ItemLike item, int count) {
            this.out = item;
            this.outCount = count;
        }

        @Nullable
        @Override
        public MerchantOffer getOffer(Entity entity, RandomSource random) {
            ItemStack in1 = new ItemStack(in, inCount);
            ItemStack out1 = new ItemStack(out, outCount);
            return new MerchantOffer(in1, out1, maxUses, experience, multiplier);
        }
    }
}