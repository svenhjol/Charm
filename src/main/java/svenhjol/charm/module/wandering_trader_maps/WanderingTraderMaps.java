package svenhjol.charm.module.wandering_trader_maps;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.*;
import svenhjol.charm.loader.CharmModule;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@CommonModule(mod = Charm.MOD_ID, description = "Wandering traders have a chance to sell maps to distant structures.")
public class WanderingTraderMaps extends CharmModule {
    private static final List<ResourceLocation> STRUCTURES = new ArrayList<>();

    private static final List<ResourceLocation> RARE_STRUCTURES = new ArrayList<>();

    @Config(name = "Maps to structures", description = "List of structure tags.")
    public static List<String> configStructures = Arrays.asList(
        "minecraft:village", "minecraft:mineshaft", "charm:swamp_hut", "charm:igloo"
    );

    @Config(name = "Maps to rare structures", description = "List of rarer structure tags.\n" +
        "These are more expensive and less likely to be sold.")
    public static List<String> configRareStructures = Arrays.asList(
        "charm:desert_pyramid", "charm:jungle_temple", "minecraft:ocean_ruin"
    );

    @Override
    public void runWhenEnabled() {
        for (String configStructure : configStructures) {
            registerStructure(new ResourceLocation(configStructure));
        }

        for (String configRareStructure : configRareStructures) {
            registerRareStructure(new ResourceLocation(configRareStructure));
        }

        if (!STRUCTURES.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                VillagerHelper.addWanderingTrade(new MapForEmeralds(), false);
            }
        }

        if (!RARE_STRUCTURES.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                VillagerHelper.addWanderingTrade(new RareMapForEmeralds(), true);
            }
        }
    }

    public static void registerStructure(ResourceLocation id) {
        STRUCTURES.add(id);
    }

    public static void registerRareStructure(ResourceLocation id) {
        RARE_STRUCTURES.add(id);
    }

    static class MapForEmeralds implements VillagerTrades.ItemListing {
        @Override
        @Nullable
        public MerchantOffer getOffer(Entity trader, Random random) {
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
        @Nullable
        public MerchantOffer getOffer(Entity trader, Random random) {
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
        @Nullable
        public ItemStack getMap(ServerLevel level, BlockPos pos, Random random) {
            var color = 0x004466;
            if (STRUCTURES.isEmpty()) return null;
            var id = STRUCTURES.get(random.nextInt(STRUCTURES.size()));

            return MapHelper.makeExplorerMap(id, level, pos, 200, color);
        }

        @Override
        public int getCost(Random random) {
            return random.nextInt(2) + 2;
        }
    }

    static class RareMap implements TraderMap {
        @Override
        @Nullable
        public ItemStack getMap(ServerLevel level, BlockPos pos, Random random) {
            var color = 0x99AA00;
            if (RARE_STRUCTURES.isEmpty()) return null;
            var id = RARE_STRUCTURES.get(random.nextInt(RARE_STRUCTURES.size()));

            return MapHelper.makeExplorerMap(id, level, pos, 500, color);
        }

        @Override
        public int getCost(Random random) {
            return random.nextInt(5) + 10;
        }
    }

    interface TraderMap {
        @Nullable
        ItemStack getMap(ServerLevel level, BlockPos pos, Random random);

        int getCost(Random random);
    }
}
