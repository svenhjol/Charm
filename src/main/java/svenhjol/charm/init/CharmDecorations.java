package svenhjol.charm.init;

import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureProcessor;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import svenhjol.charm.enums.VanillaWoodMaterial;
import svenhjol.charm.helper.LootHelper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static svenhjol.charm.helper.DecorationHelper.*;

@SuppressWarnings("ArraysAsListWithZeroOrOneArgument")
public class CharmDecorations {
    public static List<StructureProcessor> SINGLE_POOL_ELEMENT_PROCESSORS = new ArrayList<>();
    private static boolean hasAlreadySetup = false;

    public static void init() {
        if (hasAlreadySetup) return;

        List<Block> filteredFlowers = BlockTags.FLOWERS.getValues().stream()
            .filter(b -> b != Blocks.WITHER_ROSE
                && b != Blocks.FLOWERING_AZALEA_LEAVES
                && b != Blocks.FLOWERING_AZALEA
            ).collect(Collectors.toList());

        List<Block> filteredSaplings = BlockTags.SAPLINGS.getValues().stream()
            .filter(b -> b != Blocks.AZALEA
                && b != Blocks.FLOWERING_AZALEA
            ).collect(Collectors.toList());

        CARPETS.addAll(BlockTags.CARPETS.getValues());
        FLOWERS.addAll(filteredFlowers);
        FLOWER_POTS.addAll(filteredFlowers);
        SAPLINGS.addAll(filteredSaplings);
        WOOL.addAll(BlockTags.WOOL.getValues());

        VARIANT_MATERIALS.addAll(VanillaWoodMaterial.getTypes());
        OVERWORLD_VARIANT_MATERIALS.addAll(VanillaWoodMaterial.getTypesWithout(
            VanillaWoodMaterial.CRIMSON,
            VanillaWoodMaterial.WARPED
        ));

        CHEST_LOOT_TABLES = Arrays.asList(
            BuiltInLootTables.ABANDONED_MINESHAFT,
            BuiltInLootTables.DESERT_PYRAMID,
            BuiltInLootTables.JUNGLE_TEMPLE,
            BuiltInLootTables.SIMPLE_DUNGEON,
            BuiltInLootTables.SHIPWRECK_TREASURE
        );

        COMMON_LOOT_TABLES.addAll(Arrays.asList(
            BuiltInLootTables.JUNGLE_TEMPLE_DISPENSER,
            BuiltInLootTables.IGLOO_CHEST
        ));

        COMMON_LOOT_TABLES.addAll(LootHelper.getVanillaVillageLootTables());

        RARE_CHEST_LOOT_TABLES.addAll(Arrays.asList(
            BuiltInLootTables.RUINED_PORTAL,
            BuiltInLootTables.END_CITY_TREASURE
        ));

        BOOKCASE_LOOT_TABLES.addAll(Arrays.asList(
            CharmLoot.VILLAGE_LIBRARIAN
        ));

        RARE_BOOKCASE_LOOT_TABLES.addAll(Arrays.asList(
            BuiltInLootTables.STRONGHOLD_LIBRARY
        ));

        COMMON_ORES.addAll(Arrays.asList(
            Blocks.IRON_ORE,
            Blocks.COPPER_ORE,
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

        hasAlreadySetup = true;
    }
}
