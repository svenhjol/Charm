package svenhjol.charm.world.feature;

import net.minecraft.block.*;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Biomes;
import net.minecraft.init.Blocks;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.structure.MapGenVillage;
import net.minecraft.world.gen.structure.StructureVillagePieces;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.terraingen.BiomeEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import svenhjol.meson.Feature;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoreVillageBiomes extends Feature
{
    public static Map<String, List<Biome>> biomes = new HashMap<>();
    public static List<Biome> allBiomes = new ArrayList<>();

    public static String[] plainsBiomes, jungleBiomes, savannaBiomes, coldBiomes, desertBiomes;

    @Override
    public String getDescription()
    {
        return "Villages spawn in more biomes.  The type of biome determines what materials the village buildings are constructed from.";
    }

    @Override
    public void configure()
    {
        super.configure();

        coldBiomes = propStringList(
                "Cold biomes",
                "Cold biomes where villages are constructed from Taiga wood.",
                new String[] { "taiga", "mutated_taiga", "mutated_taiga_cold", "ice_flats", "mutated_ice_flats" }
        );
        jungleBiomes = propStringList(
                "Jungle biomes",
                "Jungle biomes where villages are constructed from Jungle wood.",
                new String[] { "jungle", "mutated_jungle" }
        );
        savannaBiomes = propStringList(
                "Savanna biomes",
                "Savanna biomes where villages are constructed from Acacia wood.",
                new String[] { "savanna", "mutated_savanna" }
        );
        plainsBiomes = propStringList(
                "Plains biomes",
                "Plains biomes where villages are constructed from Oak wood.",
                new String[] { "plains", "mutated_plains", "swampland", "mutated_swampland" }
        );
        desertBiomes = propStringList(
                "Desert biomes",
                "Desert biomes where villages are made from sandstone.",
                new String[] { "desert", "mutated_desert" }
        );
    }

    @Override
    public void preInit(FMLPreInitializationEvent event)
    {
        super.preInit(event);
        biomes.put("cold", makeBiomeList(coldBiomes));
        biomes.put("jungle", makeBiomeList(jungleBiomes));
        biomes.put("savanna", makeBiomeList(savannaBiomes));
        biomes.put("plains", makeBiomeList(plainsBiomes));
        biomes.put("desert", makeBiomeList(desertBiomes));

        for(String biomeSet : biomes.keySet()) {
            allBiomes.addAll(biomes.get(biomeSet));
        }

        MapGenVillage.VILLAGE_SPAWN_BIOMES = allBiomes;
    }

    private List<Biome> makeBiomeList(String[] biomes)
    {
        List<Biome> biomeList = new ArrayList<>();
        if (biomes.length > 0) {
            for (String b : biomes) {
                biomeList.add(Biome.REGISTRY.getObject(new ResourceLocation(b)));
            }
        }
        return biomeList;
    }

    @SubscribeEvent
    public void getVillageBlockID(BiomeEvent.GetVillageBlockID event)
    {
        final IBlockState original = event.getOriginal();
        Block block = original.getBlock();
        Biome biome = event.getBiome();
        if (biome == null) return;

        IBlockState replacement = null;

        BlockPlanks.EnumType planks = null;
        Block stairs = null;
        Block fence = null;
        Block door = null;

        if (biomes.get("cold").contains(biome) || biomes.get("jungle").contains(biome)) {
            planks = getWoodForBiome(biome);
            fence = getFenceForWood(planks);
            stairs = getStairsForWood(planks);
            door = getDoorForWood(planks);
        }

        if (block == Blocks.PLANKS && planks != null) {
            replacement = original
                .withProperty(BlockPlanks.VARIANT, planks);
        }

        if (block == Blocks.LOG && planks != null) {
            replacement = original
                .withProperty(BlockOldLog.VARIANT, planks)
                .withProperty(BlockLog.LOG_AXIS, original.getValue(BlockLog.LOG_AXIS));
        }

        if (block == Blocks.OAK_STAIRS && stairs != null) {
            replacement = stairs.getDefaultState()
                .withProperty(BlockStairs.FACING, original.getValue(BlockStairs.FACING))
                .withProperty(BlockStairs.HALF, original.getValue(BlockStairs.HALF))
                .withProperty(BlockStairs.SHAPE, original.getValue(BlockStairs.SHAPE));
        }

        if (block == Blocks.OAK_FENCE && fence != null) {
            replacement = fence.getDefaultState();
        }

        if (block instanceof BlockDoor && door != null) {
            replacement = door.getDefaultState();
        }
        
        if (replacement != null) {
            event.setReplacement(replacement);
            event.setResult(Result.DENY);   
        }
    }

    public static BlockPlanks.EnumType getWoodForBiome(Biome biome)
    {
        if (biomes.get("cold").contains(biome)) {
            return BlockPlanks.EnumType.SPRUCE;
        }
        if (biomes.get("jungle").contains(biome)) {
            return BlockPlanks.EnumType.JUNGLE;
        }
        if (biomes.get("savanna").contains(biome)) {
            return BlockPlanks.EnumType.ACACIA;
        }
        return BlockPlanks.EnumType.OAK;
    }

    public static BlockFence getFenceForWood(BlockPlanks.EnumType wood)
    {
        if (wood == BlockPlanks.EnumType.SPRUCE) {
            return (BlockFence)Blocks.SPRUCE_FENCE;
        }
        if (wood == BlockPlanks.EnumType.JUNGLE) {
            return (BlockFence)Blocks.JUNGLE_FENCE;
        }
        if (wood == BlockPlanks.EnumType.ACACIA) {
            return (BlockFence)Blocks.ACACIA_FENCE;
        }
        return (BlockFence)Blocks.OAK_FENCE;
    }

    public static BlockStairs getStairsForWood(BlockPlanks.EnumType wood)
    {
        if (wood == BlockPlanks.EnumType.SPRUCE) {
            return (BlockStairs) Blocks.SPRUCE_STAIRS;
        }
        if (wood == BlockPlanks.EnumType.JUNGLE) {
            return (BlockStairs) Blocks.JUNGLE_STAIRS;
        }
        if (wood == BlockPlanks.EnumType.ACACIA) {
            return (BlockStairs) Blocks.ACACIA_STAIRS;
        }
        return (BlockStairs) Blocks.OAK_STAIRS;
    }

    public static BlockDoor getDoorForWood(BlockPlanks.EnumType wood)
    {
        if (wood == BlockPlanks.EnumType.SPRUCE) {
            return Blocks.SPRUCE_DOOR;
        }
        if (wood == BlockPlanks.EnumType.JUNGLE) {
            return Blocks.JUNGLE_DOOR;
        }
        if (wood == BlockPlanks.EnumType.ACACIA) {
            return Blocks.ACACIA_DOOR;
        }
        return Blocks.OAK_DOOR;
    }

    public static BlockDoor villageDoorsForBiome(StructureVillagePieces.Start start)
    {
        if (start == null || start.biome == null) return Blocks.OAK_DOOR;

        Biome biome = start.biome;
        IBlockState door = Blocks.OAK_DOOR.getDefaultState();

        // call the forge hook to get replacement blocks based on biome
        BiomeEvent.GetVillageBlockID event = new BiomeEvent.GetVillageBlockID(biome, door);
        MinecraftForge.TERRAIN_GEN_BUS.post(event);
        if (event.getResult() == Result.DENY) {
            return (BlockDoor) event.getReplacement().getBlock();
        }

        if (biome == Biomes.SAVANNA) door = Blocks.ACACIA_DOOR.getDefaultState();
        if (biome == Biomes.TAIGA) door = Blocks.SPRUCE_DOOR.getDefaultState();

        return (BlockDoor) door.getBlock();
    }

    @Override
    public boolean hasTerrainSubscriptions()
    {
        return true;
    }
}