package svenhjol.charm.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.CampfireBlock;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.village.TradeOffer;
import net.minecraft.village.TradeOffers;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.BiomeKeys;
import net.minecraft.world.gen.feature.StructureFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.helper.BiomeHelper;
import svenhjol.charm.base.helper.MapHelper;
import svenhjol.charm.base.helper.VillagerHelper;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Module(mod = Charm.MOD_ID, description = "Wandering traders only appear near signal campfires and sell maps to biomes and structures.")
public class WanderingTraderImprovements extends CharmModule {
    public static final List<TraderMap> traderMaps = new ArrayList<>();

    @Config(name = "Spawn near signal fire", description = "If true, wandering traders will only spawn if the player is near a signal fire.")
    public static boolean spawnNearSignalFire = true;

    @Config(name = "Trade biome maps", description = "If true, wandering traders will sell maps to biomes.")
    public static boolean tradeBiomeMaps = true;

    @Config(name = "Trade structure maps", description = "If true, wandering traders will sell maps to structures.")
    public static boolean tradeStructureMaps = true;

    @Config(name = "Frequent spawning", description = "If true, makes wandering traders more likely to spawn after one Minecraft day.")
    public static boolean frequentSpawn = false;

    @Override
    public void init() {
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
                new StructureMap(StructureFeature.JUNGLE_PYRAMID, true), // jungle temple
                new StructureMap(StructureFeature.DESERT_PYRAMID, true) // desert pyramid
            ));
        }

        if (tradeBiomeMaps) {
            traderMaps.addAll(Arrays.asList(
                new BiomeMap(BiomeKeys.WARM_OCEAN, false),
                new BiomeMap(BiomeKeys.SNOWY_TUNDRA, false),
                new BiomeMap(BiomeKeys.DESERT, false),
                new BiomeMap(BiomeKeys.SUNFLOWER_PLAINS, false),
                new BiomeMap(BiomeKeys.FROZEN_OCEAN, false),
                new BiomeMap(BiomeKeys.BADLANDS, true),
                new BiomeMap(BiomeKeys.FLOWER_FOREST, true),
                new BiomeMap(BiomeKeys.MUSHROOM_FIELDS, true),
                new BiomeMap(BiomeKeys.BAMBOO_JUNGLE, true),
                new BiomeMap(BiomeKeys.ICE_SPIKES, true)
            ));
        }

        for (int i = 0; i < 3; i++)
            VillagerHelper.addWanderingTrade(new StructureMapForEmeraldsTrade(), false);
    }

    public static boolean checkSpawnConditions(World world, BlockPos pos) {
        if (!ModuleHandler.enabled("charm:wandering_trader_improvements"))
            return true;

        if (!spawnNearSignalFire)
            return true;

        BlockPos pos1 = pos.add(-24, -24, -24);
        BlockPos pos2 = pos.add(24, 24, 24);

        boolean foundFire = BlockPos.stream(pos1, pos2).anyMatch(p -> {
            BlockState state = world.getBlockState(p);
            return state.getBlock() instanceof CampfireBlock
                && state.contains(CampfireBlock.SIGNAL_FIRE)
                && state.get(CampfireBlock.SIGNAL_FIRE);
        });

        Charm.LOG.debug(foundFire
            ? "Found signal fire within range of player, attempting to spawn Wandering Trader."
            : "No signal fire within range of player, not spawning Wandering Trader.");

        return foundFire;
    }

    public static boolean shouldSpawnFrequently() {
        return ModuleHandler.enabled("charm:wandering_trader_improvements") && frequentSpawn;
    }

    public static class StructureMapForEmeraldsTrade implements TradeOffers.Factory {
        @Override
        public TradeOffer create(Entity trader, Random rand) {
            TraderMap traderMap = traderMaps.get(rand.nextInt(traderMaps.size()));

            if (!trader.world.isClient) {
                ItemStack map = traderMap.getMap((ServerWorld) trader.world, trader.getBlockPos());
                if (map != null) {
                    ItemStack in1 = new ItemStack(Items.EMERALD, traderMap.getCost(rand));
                    ItemStack in2 = new ItemStack(Items.COMPASS);
                    return new TradeOffer(in1, in2, map, 1, 5, 0.2F);
                }
            }

            return null;
        }
    }

    public interface TraderMap {
        ItemStack getMap(ServerWorld world, BlockPos pos);

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
        public ItemStack getMap(ServerWorld world, BlockPos pos) {
            int color = 0x662200;
            BlockPos nearestStructure = world.locateStructure(structure, pos, 2000, true);
            if (nearestStructure == null)
                return null;

            TranslatableText structureName = new TranslatableText("structure.charm." + structure.getName());
            TranslatableText mapName = new TranslatableText("filled_map.charm.trader_map", structureName);
            return MapHelper.getMap(world, nearestStructure, mapName, MapIcon.Type.TARGET_X, color);
        }

        @Override
        public int getCost(Random rand) {
            return rare ? rand.nextInt(4) + 6 : rand.nextInt(2) + 2;
        }
    }

    public static class BiomeMap implements TraderMap {
        public RegistryKey<Biome> biomeKey;
        public boolean rare;

        public BiomeMap(RegistryKey<Biome> biomeKey, boolean rare) {
            this.biomeKey = biomeKey;
            this.rare = rare;
        }

        @Override
        public ItemStack getMap(ServerWorld world, BlockPos pos) {
            int color = 0x002266;

            BlockPos nearestBiome = BiomeHelper.locateBiome(biomeKey, world, pos);
            String biomeName = biomeKey.getValue().getPath();

            if (nearestBiome == null)
                return null;

            TranslatableText mapName = new TranslatableText("filled_map.charm.trader_map", new TranslatableText("biome.minecraft." + biomeName));
            return MapHelper.getMap(world, nearestBiome, mapName, MapIcon.Type.TARGET_X, color);
        }

        @Override
        public int getCost(Random rand) {
            return rare ? rand.nextInt(3) + 3 : rand.nextInt(1) + 1;
        }
    }
}
