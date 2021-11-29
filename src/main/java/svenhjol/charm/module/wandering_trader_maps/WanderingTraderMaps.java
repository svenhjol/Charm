package svenhjol.charm.module.wandering_trader_maps;

import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.BiomeHelper;
import svenhjol.charm.helper.MapHelper;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@CommonModule(mod = Charm.MOD_ID, description = "Wandering traders have a chance to sell maps to biomes and structures.")
public class WanderingTraderMaps extends CharmModule {
    public static final List<TraderMap> traderMaps = new ArrayList<>();

    @Config(name = "Trade biome maps", description = "If true, wandering traders will sell maps to biomes.")
    public static boolean tradeBiomeMaps = true;

    @Config(name = "Trade structure maps", description = "If true, wandering traders will sell maps to structures.")
    public static boolean tradeStructureMaps = true;

    @Override
    public void runWhenEnabled() {
        if (tradeStructureMaps) {
            traderMaps.addAll(Arrays.asList(
                new StructureMap(StructureFeature.RUINED_PORTAL, false),
                new StructureMap(StructureFeature.VILLAGE, false),
                new StructureMap(StructureFeature.SWAMP_HUT, false),
                new StructureMap(StructureFeature.SHIPWRECK, false),
                new StructureMap(StructureFeature.PILLAGER_OUTPOST, false),
                new StructureMap(StructureFeature.MINESHAFT, false),
                new StructureMap(StructureFeature.IGLOO, true),
                new StructureMap(StructureFeature.OCEAN_RUIN, true),
                new StructureMap(StructureFeature.JUNGLE_TEMPLE, true),
                new StructureMap(StructureFeature.DESERT_PYRAMID, true)
            ));
        }

        if (tradeBiomeMaps) {
            traderMaps.addAll(Arrays.asList(
                new BiomeMap(Biomes.WARM_OCEAN, false),
                new BiomeMap(Biomes.SNOWY_PLAINS, false),
                new BiomeMap(Biomes.DESERT, false),
                new BiomeMap(Biomes.SUNFLOWER_PLAINS, false),
                new BiomeMap(Biomes.FROZEN_OCEAN, false),
                new BiomeMap(Biomes.BADLANDS, true),
                new BiomeMap(Biomes.FLOWER_FOREST, true),
                new BiomeMap(Biomes.MUSHROOM_FIELDS, true),
                new BiomeMap(Biomes.BAMBOO_JUNGLE, true),
                new BiomeMap(Biomes.ICE_SPIKES, true),
                new BiomeMap(Biomes.JAGGED_PEAKS, true)
            ));
        }

        for (int i = 0; i < 3; i++) {
            VillagerHelper.addWanderingTrade(new StructureMapForEmeraldsTrade(), false);
        }
    }

    public static class StructureMapForEmeraldsTrade implements VillagerTrades.ItemListing {
        @Override
        public MerchantOffer getOffer(Entity trader, Random random) {
            if (!trader.level.isClientSide && !traderMaps.isEmpty()) {
                TraderMap traderMap = traderMaps.get(random.nextInt(traderMaps.size()));
                ItemStack map = traderMap.getMap((ServerLevel) trader.level, trader.blockPosition());
                if (map != null) {
                    ItemStack in1 = new ItemStack(Items.EMERALD, traderMap.getCost(random));
                    ItemStack in2 = new ItemStack(Items.COMPASS);
                    return new MerchantOffer(in1, in2, map, 1, 5, 0.2F);
                }
            }

            return null;
        }
    }

    public interface TraderMap {
        ItemStack getMap(ServerLevel level, BlockPos pos);

        int getCost(Random rand);
    }

    public static class StructureMap implements TraderMap {
        public StructureFeature<?> structure;
        public boolean rare;

        public StructureMap(StructureFeature<?> structure, boolean rare) {
            this.structure = structure;
            this.rare = rare;
        }

        @Override
        public ItemStack getMap(ServerLevel level, BlockPos pos) {
            int color = 0x662200;
            BlockPos nearestStructure = level.findNearestMapFeature(structure, pos, 2000, true);
            if (nearestStructure == null) return null;

            TranslatableComponent structureName = new TranslatableComponent("structure.charm." + structure.getFeatureName());
            TranslatableComponent mapName = new TranslatableComponent("filled_map.charm.trader_map", structureName);
            return MapHelper.create(level, nearestStructure, mapName, MapDecoration.Type.TARGET_X, color);
        }

        @Override
        public int getCost(Random rand) {
            return rare ? rand.nextInt(4) + 6 : rand.nextInt(2) + 2;
        }
    }

    public static class BiomeMap implements TraderMap {
        public ResourceKey<Biome> biomeKey;
        public boolean rare;

        public BiomeMap(ResourceKey<Biome> biomeKey, boolean rare) {
            this.biomeKey = biomeKey;
            this.rare = rare;
        }

        @Override
        public ItemStack getMap(ServerLevel level, BlockPos pos) {
            int color = 0x002266;

            BlockPos nearestBiome = BiomeHelper.locateBiome(biomeKey, level, pos);
            String biomeName = biomeKey.location().getPath();

            if (nearestBiome == null) return null;

            TranslatableComponent mapName = new TranslatableComponent("filled_map.charm.trader_map", new TranslatableComponent("biome.minecraft." + biomeName));
            return MapHelper.create(level, nearestBiome, mapName, MapDecoration.Type.TARGET_X, color);
        }

        @Override
        public int getCost(Random rand) {
            return rare ? rand.nextInt(3) + 3 : rand.nextInt(1) + 1;
        }
    }
}
