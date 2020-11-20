package svenhjol.charm.base.handler;

import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.loot.LootTables;
import net.minecraft.tag.BlockTags;
import svenhjol.charm.base.enums.VanillaVariantMaterial;
import svenhjol.charm.base.helper.LootHelper;

import java.util.Arrays;

import static svenhjol.charm.base.helper.DecorationHelper.*;

public class DecorationHandler {
    private static boolean hasInit = false;

    public static void init() {
        if (hasInit)
            return;

        CARPETS.addAll(BlockTags.CARPETS.values());
        FLOWERS.addAll(BlockTags.FLOWERS.values());
        FLOWER_POTS.addAll(BlockTags.FLOWER_POTS.values());
        SAPLINGS.addAll(BlockTags.SAPLINGS.values());
        WOOL.addAll(BlockTags.WOOL.values());

        VARIANT_MATERIALS.addAll(VanillaVariantMaterial.getTypes());

        CHEST_LOOT_TABLES = Arrays.asList(
            LootTables.ABANDONED_MINESHAFT_CHEST,
            LootTables.DESERT_PYRAMID_CHEST,
            LootTables.JUNGLE_TEMPLE_CHEST,
            LootTables.SIMPLE_DUNGEON_CHEST,
            LootTables.STRONGHOLD_CORRIDOR_CHEST
        );

        COMMON_LOOT_TABLES.addAll(Arrays.asList(
            LootTables.JUNGLE_TEMPLE_DISPENSER_CHEST,
            LootTables.IGLOO_CHEST_CHEST
        ));

        COMMON_LOOT_TABLES.addAll(LootHelper.getVanillaVillageLootTables());

        RARE_CHEST_LOOT_TABLES.addAll(Arrays.asList(
            LootTables.RUINED_PORTAL_CHEST
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

        hasInit = true;
    }
}
