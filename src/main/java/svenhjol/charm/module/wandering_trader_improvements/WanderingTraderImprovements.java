package svenhjol.charm.module.wandering_trader_improvements;

import svenhjol.charm.Charm;
import svenhjol.charm.helper.BiomeHelper;
import svenhjol.charm.helper.MapHelper;
import svenhjol.charm.helper.VillagerHelper;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.CommonModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
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
import svenhjol.charm.loader.CharmCommonModule;

@CommonModule(mod = Charm.MOD_ID, description = "Wandering traders only appear near signal campfires and sell maps to biomes and structures.")
public class WanderingTraderImprovements extends CharmCommonModule {
    public static final List<TraderMap> traderMaps = new ArrayList<>();

    @Config(name = "Trade biome maps", description = "If true, wandering traders will sell maps to biomes.")
    public static boolean tradeBiomeMaps = true;

    @Config(name = "Trade structure maps", description = "If true, wandering traders will sell maps to structures.")
    public static boolean tradeStructureMaps = true;

    @Config(name = "Frequent spawning", description = "If true, makes wandering traders more likely to spawn after one Minecraft day.")
    public static boolean frequentSpawn = false;

    @Override
    public void run() {
        if (tradeStructureMaps) {
            traderMaps.addAll(Arrays.asList(
                new StructureMap(StructureFeature.RUINED_PORTAL, false), // ruined portal
                new StructureMap(StructureFeature.VILLAGE, false), // village
                new StructureMap(StructureFeature.SWAMP_HUT, false), // swamp hut
                new StructureMap(StructureFeature.SHIPWRECK, false), // shipwreck
                new StructureMap(StructureFeature.OCEAN_RUIN, false), // ocean ruin
                new StructureMap(StructureFeature.PILLAGER_OUTPOST, false), // pillager outpost
                new StructureMap(StructureFeature.MINESHAFT, false), // mineshaft
                new StructureMap(StructureFeature.IGLOO, false), // igloo
                new StructureMap(StructureFeature.JUNGLE_TEMPLE, true), // jungle temple
                new StructureMap(StructureFeature.DESERT_PYRAMID, true) // desert pyramid
            ));
        }

        if (tradeBiomeMaps) {
            traderMaps.addAll(Arrays.asList(
                new BiomeMap(Biomes.WARM_OCEAN, false),
                new BiomeMap(Biomes.SNOWY_TUNDRA, false),
                new BiomeMap(Biomes.DESERT, false),
                new BiomeMap(Biomes.SUNFLOWER_PLAINS, false),
                new BiomeMap(Biomes.FROZEN_OCEAN, false),
                new BiomeMap(Biomes.BADLANDS, true),
                new BiomeMap(Biomes.FLOWER_FOREST, true),
                new BiomeMap(Biomes.MUSHROOM_FIELDS, true),
                new BiomeMap(Biomes.BAMBOO_JUNGLE, true),
                new BiomeMap(Biomes.ICE_SPIKES, true)
            ));
        }

        for (int i = 0; i < 3; i++)
            VillagerHelper.addWanderingTrade(new StructureMapForEmeraldsTrade(), false);
    }

    public static boolean shouldSpawnFrequently() {
        return Charm.LOADER.isEnabled("charm:wandering_trader_improvements") && frequentSpawn;
    }

    public static class StructureMapForEmeraldsTrade implements VillagerTrades.ItemListing {
        @Override
        public MerchantOffer getOffer(Entity trader, Random rand) {
            if (!trader.level.isClientSide && !traderMaps.isEmpty()) {
                TraderMap traderMap = traderMaps.get(rand.nextInt(traderMaps.size()));
                ItemStack map = traderMap.getMap((ServerLevel) trader.level, trader.blockPosition());
                if (map != null) {
                    ItemStack in1 = new ItemStack(Items.EMERALD, traderMap.getCost(rand));
                    ItemStack in2 = new ItemStack(Items.COMPASS);
                    return new MerchantOffer(in1, in2, map, 1, 5, 0.2F);
                }
            }

            return null;
        }
    }

    public interface TraderMap {
        ItemStack getMap(ServerLevel world, BlockPos pos);

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
        public ItemStack getMap(ServerLevel world, BlockPos pos) {
            int color = 0x662200;
            BlockPos nearestStructure = world.findNearestMapFeature(structure, pos, 2000, true);
            if (nearestStructure == null)
                return null;

            TranslatableComponent structureName = new TranslatableComponent("structure.charm." + structure.getFeatureName());
            TranslatableComponent mapName = new TranslatableComponent("filled_map.charm.trader_map", structureName);
            return MapHelper.getMap(world, nearestStructure, mapName, MapDecoration.Type.TARGET_X, color);
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
        public ItemStack getMap(ServerLevel world, BlockPos pos) {
            int color = 0x002266;

            BlockPos nearestBiome = BiomeHelper.locateBiome(biomeKey, world, pos);
            String biomeName = biomeKey.location().getPath();

            if (nearestBiome == null)
                return null;

            TranslatableComponent mapName = new TranslatableComponent("filled_map.charm.trader_map", new TranslatableComponent("biome.minecraft." + biomeName));
            return MapHelper.getMap(world, nearestBiome, mapName, MapDecoration.Type.TARGET_X, color);
        }

        @Override
        public int getCost(Random rand) {
            return rare ? rand.nextInt(3) + 3 : rand.nextInt(1) + 1;
        }
    }
}
