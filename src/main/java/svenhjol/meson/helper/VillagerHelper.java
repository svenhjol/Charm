package svenhjol.meson.helper;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;

import java.util.Arrays;
import java.util.List;

public class VillagerHelper {
    public static void addTrade(VillagerProfession profession, int level, TradeOffers.Factory trade) {
        Int2ObjectMap<TradeOffers.Factory[]> fixedTrades = TradeOffers.PROFESSION_TO_LEVELED_TRADE.get(profession);
        Int2ObjectMap<List<TradeOffers.Factory>> mutableTrades = new Int2ObjectOpenHashMap<>();

        for (int i = 1; i <= 5; i++) {
            mutableTrades.put(i, DefaultedList.of());
        }

        fixedTrades.int2ObjectEntrySet().forEach(e -> {
            Arrays.stream(e.getValue()).forEach(a -> mutableTrades.get(e.getIntKey()).add(a));
        });

        mutableTrades.get(level).add(trade);

        Int2ObjectMap<TradeOffers.Factory[]> mappedTrades = new Int2ObjectOpenHashMap<>();
        mutableTrades.int2ObjectEntrySet().forEach(e -> mappedTrades.put(e.getIntKey(), e.getValue().toArray(new TradeOffers.Factory[0])));
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(profession, mappedTrades);
    }

    public static void addWanderingTrade(TradeOffers.Factory trade, boolean isRare) {
        if (isRare) {
            List<TradeOffers.Factory> rareTrades = DefaultedList.of();
            rareTrades.addAll(Arrays.asList(TradeOffers.WANDERING_TRADER_TRADES.get(2)));
            rareTrades.add(trade);
            TradeOffers.WANDERING_TRADER_TRADES.put(2, rareTrades.toArray(new TradeOffers.Factory[0]));
        } else {
            List<TradeOffers.Factory> normalTrades = DefaultedList.of();
            normalTrades.addAll(Arrays.asList(TradeOffers.WANDERING_TRADER_TRADES.get(1)));
            normalTrades.add(trade);
            TradeOffers.WANDERING_TRADER_TRADES.put(1, normalTrades.toArray(new TradeOffers.Factory[0]));
        }
    }
}
