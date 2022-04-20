package svenhjol.charm.module.wandering_trader_maps;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.MapHelper;
import svenhjol.charm.helper.StringHelper;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.helper.WorldHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.Arrays;
import java.util.List;

@CommonModule(mod = Charm.MOD_ID, description = "Wandering traders have a chance to sell maps to distant structures.")
public class WanderingTraderMaps extends CharmModule {
    @Config(name = "Structures", description = "List of structure IDs and structure tags.")
    public static List<String> structures = Arrays.asList(
        "#minecraft:village", "#minecraft:mineshaft", "minecraft:swamp_hut", "minecraft:igloo"
    );

    @Config(name = "Rare structures", description = "List of rarer structure IDs and structure tags.\n" +
        "These are more expensive and less likely to be sold.")
    public static List<String> rareStructures = Arrays.asList(
        "minecraft:desert_pyramid", "minecraft:jungle_temple", "#minecraft:ocean_ruin", "minecraft:ancient_city"
    );

    @Override
    public void runWhenEnabled() {
        for (int i = 0; i < 3; i++) {
            VillagerHelper.addWanderingTrade(new MapForEmeralds(), false);
        }
        for (int i = 0; i < 3; i++) {
            VillagerHelper.addWanderingTrade(new RareMapForEmeralds(), true);
        }
    }

    public static ItemStack makeTraderMap(String id, ServerLevel level, BlockPos pos, int distance, int color) {
        var nearest = WorldHelper.findNearestMapFeature(id, level, pos, distance, true);
        if (nearest == null) return new ItemStack(Items.MAP);

        var name = StringHelper.snakeToPretty(id.substring(id.indexOf(":") + 1), true);
        var mapName = Component.translatable("filled_map.charm.trader_map", Component.literal(name));
        return MapHelper.create(level, nearest, mapName, MapDecoration.Type.TARGET_X, color);
    }

    static class MapForEmeralds implements VillagerTrades.ItemListing {
        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            if (!trader.level.isClientSide) {
                var map = new Map();
                var stack = map.getMap((ServerLevel) trader.level, trader.blockPosition(), random);

                if (stack != null) {
                    var in1 = new ItemStack(Items.EMERALD, map.getCost(random));
                    var in2 = new ItemStack(Items.COMPASS);
                    return new MerchantOffer(in1, in2, stack, 1, 5, 0.2F);
                }
            }

            return null;
        }
    }

    static class RareMapForEmeralds implements VillagerTrades.ItemListing {
        @Override
        public MerchantOffer getOffer(Entity trader, RandomSource random) {
            if (!trader.level.isClientSide) {
                var map = new RareMap();
                var stack = map.getMap((ServerLevel) trader.level, trader.blockPosition(), random);

                if (stack != null) {
                    var in1 = new ItemStack(Items.EMERALD, map.getCost(random));
                    var in2 = new ItemStack(Items.COMPASS);
                    return new MerchantOffer(in1, in2, stack, 1, 5, 0.2F);
                }
            }

            return null;
        }
    }

    static class Map implements TraderMap {
        @Override
        public ItemStack getMap(ServerLevel level, BlockPos pos, RandomSource random) {
            var color = 0x004466;
            var id = structures.get(random.nextInt(structures.size()));

            return makeTraderMap(id, level, pos, 200, color);
        }

        @Override
        public int getCost(RandomSource random) {
            return random.nextInt(2) + 2;
        }
    }

    static class RareMap implements TraderMap {
        @Override
        public ItemStack getMap(ServerLevel level, BlockPos pos, RandomSource random) {
            var color = 0x99AA00;
            var id = rareStructures.get(random.nextInt(rareStructures.size()));

            return makeTraderMap(id, level, pos, 500, color);
        }

        @Override
        public int getCost(RandomSource random) {
            return random.nextInt(5) + 10;
        }
    }

    interface TraderMap {
        ItemStack getMap(ServerLevel level, BlockPos pos, RandomSource random);

        int getCost(RandomSource random);
    }
}
