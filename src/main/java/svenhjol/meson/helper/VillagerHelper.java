package svenhjol.meson.helper;

import com.google.common.collect.ImmutableSet;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import net.fabricmc.fabric.mixin.object.builder.PointOfInterestTypeAccessor;
import net.fabricmc.fabric.mixin.object.builder.VillagerProfessionAccessor;
import net.minecraft.block.Block;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.collection.DefaultedList;
import net.minecraft.util.registry.Registry;
import net.minecraft.village.TradeOffers;
import net.minecraft.village.VillagerProfession;
import net.minecraft.world.poi.PointOfInterestType;

import java.util.Arrays;
import java.util.List;

public class VillagerHelper {
    public static VillagerProfession addProfession(Identifier id, PointOfInterestType poit, SoundEvent worksound) {
        VillagerProfession profession = VillagerProfessionAccessor.create(id.toString(), poit, ImmutableSet.of(), ImmutableSet.of(), worksound);
        VillagerProfession registeredProfession = Registry.register(Registry.VILLAGER_PROFESSION, id, profession);
        TradeOffers.PROFESSION_TO_LEVELED_TRADE.put(profession, new Int2ObjectOpenHashMap<>());
        return registeredProfession;
    }

    public static PointOfInterestType addPointOfInterestType(Identifier id, Block block, int ticketCount) {
        PointOfInterestType poit = PointOfInterestTypeAccessor.callCreate(id.toString(), ImmutableSet.copyOf(block.getStateManager().getStates()), ticketCount, 1);
        Registry.register(Registry.POINT_OF_INTEREST_TYPE, id, poit);
        return PointOfInterestTypeAccessor.callSetup(poit);
    }

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
