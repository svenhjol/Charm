package svenhjol.meson.helper;

import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTables;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import svenhjol.meson.enums.IVariantMaterial;
import svenhjol.meson.enums.VanillaVariantMaterial;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class DecorationHelper {
    public static List<Block> BARK = new ArrayList<>();
    public static List<Identifier> BOOKCASE_LOOT_TABLES = new ArrayList<>();
    public static List<Block> CARPETS = new ArrayList<>();
    public static List<Identifier> CHEST_LOOT_TABLES = new ArrayList<>();
    public static List<Identifier> COMMON_LOOT_TABLES = new ArrayList<>();
    public static List<Block> COMMON_ORES = new ArrayList<>();
    public static List<Block> DECORATION_BLOCKS = new ArrayList<>();
    public static List<Block> FLOWERS = new ArrayList<>();
    public static List<Block> FLOWER_POTS = new ArrayList<>();
    public static List<Identifier> RARE_LOOT_TABLES = new ArrayList<>();
    public static List<Block> RARE_ORES = new ArrayList<>();
    public static List<Block> SAPLINGS = new ArrayList<>();
    public static List<EntityType<?>> SPAWNER_MOBS = new ArrayList<>();
    public static List<Block> STRIPPED_LOGS = new ArrayList<>();
    public static List<Block> STRIPPED_WOOD = new ArrayList<>();
    public static List<IVariantMaterial> VARIANT_MATERIALS = new ArrayList<>();
    public static List<Block> WOOL = new ArrayList<>();

    public static void init() {
        CARPETS.addAll(BlockTags.CARPETS.values());
        FLOWERS.addAll(BlockTags.FLOWERS.values());
        FLOWER_POTS.addAll(BlockTags.FLOWER_POTS.values());
        SAPLINGS.addAll(BlockTags.SAPLINGS.values());
        WOOL.addAll(BlockTags.WOOL.values());

        VARIANT_MATERIALS.addAll(VanillaVariantMaterial.getTypes());

        BARK.addAll(Arrays.asList(
            Blocks.ACACIA_WOOD,
            Blocks.BIRCH_WOOD,
            Blocks.DARK_OAK_WOOD,
            Blocks.JUNGLE_WOOD,
            Blocks.OAK_WOOD,
            Blocks.SPRUCE_WOOD
        ));

        STRIPPED_LOGS.addAll(Arrays.asList(
            Blocks.STRIPPED_ACACIA_LOG,
            Blocks.STRIPPED_BIRCH_LOG,
            Blocks.STRIPPED_DARK_OAK_LOG,
            Blocks.STRIPPED_JUNGLE_LOG,
            Blocks.STRIPPED_OAK_LOG,
            Blocks.STRIPPED_SPRUCE_LOG
        ));

        STRIPPED_WOOD.addAll(Arrays.asList(
            Blocks.STRIPPED_ACACIA_WOOD,
            Blocks.STRIPPED_BIRCH_WOOD,
            Blocks.STRIPPED_DARK_OAK_WOOD,
            Blocks.STRIPPED_JUNGLE_WOOD,
            Blocks.STRIPPED_OAK_WOOD,
            Blocks.STRIPPED_SPRUCE_WOOD
        ));

        CHEST_LOOT_TABLES.addAll(Arrays.asList(
            LootTables.ABANDONED_MINESHAFT_CHEST,
            LootTables.BURIED_TREASURE_CHEST,
            LootTables.DESERT_PYRAMID_CHEST,
            LootTables.JUNGLE_TEMPLE_CHEST,
            LootTables.SIMPLE_DUNGEON_CHEST,
            LootTables.STRONGHOLD_CORRIDOR_CHEST
        ));

        COMMON_LOOT_TABLES.addAll(Arrays.asList(
            LootTables.JUNGLE_TEMPLE_DISPENSER_CHEST,
            LootTables.IGLOO_CHEST_CHEST
        ));

        COMMON_LOOT_TABLES.addAll(LootHelper.getVanillaVillageLootTables());

        RARE_LOOT_TABLES.addAll(Arrays.asList(
            LootTables.STRONGHOLD_LIBRARY_CHEST,
            LootTables.NETHER_BRIDGE_CHEST,
            LootTables.END_CITY_TREASURE_CHEST,
            LootTables.RUINED_PORTAL_CHEST,
            LootTables.WOODLAND_MANSION_CHEST,
            LootTables.UNDERWATER_RUIN_BIG_CHEST
        ));

        BOOKCASE_LOOT_TABLES.addAll(Arrays.asList(
            LootTables.STRONGHOLD_LIBRARY_CHEST
        ));

        COMMON_ORES.addAll(Arrays.asList(
            Blocks.IRON_ORE,
            Blocks.COAL_ORE,
            Blocks.REDSTONE_ORE
        ));

        RARE_ORES.addAll(Arrays.asList(
            Blocks.GOLD_ORE,
            Blocks.DIAMOND_ORE,
            Blocks.EMERALD_ORE
        ));

        DECORATION_BLOCKS.addAll(Arrays.asList(
            Blocks.ANVIL,
            Blocks.BELL,
            Blocks.BLAST_FURNACE,
            Blocks.BONE_BLOCK,
            Blocks.BREWING_STAND,
            Blocks.CARTOGRAPHY_TABLE,
            Blocks.CARVED_PUMPKIN,
            Blocks.CAULDRON,
            Blocks.CHIPPED_ANVIL,
            Blocks.COAL_BLOCK,
            Blocks.COBWEB,
            Blocks.COMPOSTER,
            Blocks.CRAFTING_TABLE,
            Blocks.DAMAGED_ANVIL,
            Blocks.DISPENSER,
            Blocks.FLETCHING_TABLE,
            Blocks.FURNACE,
            Blocks.HAY_BLOCK,
            Blocks.IRON_BLOCK,
            Blocks.JUKEBOX,
            Blocks.LANTERN,
            Blocks.LAPIS_BLOCK,
            Blocks.LECTERN,
            Blocks.MELON,
            Blocks.NOTE_BLOCK,
            Blocks.OBSERVER,
            Blocks.POLISHED_ANDESITE,
            Blocks.POLISHED_DIORITE,
            Blocks.POLISHED_GRANITE,
            Blocks.PUMPKIN,
            Blocks.SLIME_BLOCK,
            Blocks.SMITHING_TABLE,
            Blocks.SMOKER,
            Blocks.STONECUTTER
        ));

        DECORATION_BLOCKS.addAll(WOOL);
        DECORATION_BLOCKS.addAll(FLOWER_POTS);

        SPAWNER_MOBS.addAll(Arrays.asList(
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.SPIDER,
            EntityType.CAVE_SPIDER
        ));
    }

    public static Identifier getRandomLootTable(List<Identifier> lootTables, Random random) {
        return lootTables.get(random.nextInt(lootTables.size()));
    }

    public static BlockState getRandomBlock(List<Block> blocks, Random random) {
        return getRandomBlock(blocks, random, null);
    }

    public static BlockState getRandomBlock(List<Block> blocks, Random random, @Nullable Direction facing) {
        if (blocks.size() == 0)
            return Blocks.AIR.getDefaultState();

        Block block = blocks.get(random.nextInt(blocks.size()));
        BlockState state = block.getDefaultState();

        if (blocks == DECORATION_BLOCKS) {
            // sort out orientation
            if (facing != null) {
                if (block == Blocks.BLAST_FURNACE)
                    state = state.with(BlastFurnaceBlock.FACING, facing);
                if (block == Blocks.CARVED_PUMPKIN)
                    state = state.with(CarvedPumpkinBlock.FACING, facing);
                if (block == Blocks.DISPENSER)
                    state = state.with(DispenserBlock.FACING, facing);
                if (block == Blocks.FURNACE)
                    state = state.with(FurnaceBlock.FACING, facing);
                if (block == Blocks.LECTERN)
                    state = state.with(LecternBlock.FACING, facing);
                if (block == Blocks.OBSERVER)
                    state = state.with(ObserverBlock.FACING, facing);
                if (block == Blocks.SMOKER)
                    state = state.with(SmokerBlock.FACING, facing);
            }

            // some blocks have custom state, randomize it here
            if (block == Blocks.CAMPFIRE)
                state = state.with(CampfireBlock.LIT, false);
            if (block == Blocks.CAULDRON)
                state = state.with(CauldronBlock.LEVEL, random.nextInt(3));
            if (block == Blocks.COMPOSTER)
                state = state.with(ComposterBlock.LEVEL, random.nextInt(7));
        }

        return state;
    }

    public static IVariantMaterial getRandomVariantMaterial(Random random) {
        return VARIANT_MATERIALS.get(random.nextInt(VARIANT_MATERIALS.size()));
    }
}
