package svenhjol.charm.helper;

import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import svenhjol.charm.enums.IVariantMaterial;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

public class DecorationHelper {
    public static List<Block> BARK = new ArrayList<>();
    public static List<ResourceLocation> BOOKCASE_LOOT_TABLES = new ArrayList<>();
    public static List<Block> CARPETS = new ArrayList<>();
    public static List<ResourceLocation> CHEST_LOOT_TABLES = new ArrayList<>();
    public static List<ResourceLocation> COMMON_LOOT_TABLES = new ArrayList<>();
    public static List<Block> COMMON_ORES = new ArrayList<>();
    public static List<Block> DECORATION_BLOCKS = new ArrayList<>();
    public static List<Block> FLOWERS = new ArrayList<>();
    public static List<Block> FLOWER_POTS = new ArrayList<>();
    public static List<ResourceLocation> RARE_BOOKCASE_LOOT_TABLES = new ArrayList<>();
    public static List<ResourceLocation> RARE_CHEST_LOOT_TABLES = new ArrayList<>();
    public static List<Block> RARE_ORES = new ArrayList<>();
    public static List<Block> SAPLINGS = new ArrayList<>();
    public static List<EntityType<?>> SPAWNER_MOBS = new ArrayList<>();
    public static List<Block> STRIPPED_LOGS = new ArrayList<>();
    public static List<Block> STRIPPED_WOOD = new ArrayList<>();
    public static List<IVariantMaterial> VARIANT_MATERIALS = new ArrayList<>();
    public static List<IVariantMaterial> OVERWORLD_VARIANT_MATERIALS = new ArrayList<>();
    public static List<Block> WOOL = new ArrayList<>();

    public static Map<Block, Function<Direction, BlockState>> STATE_CALLBACK = new HashMap<>();

    public static ResourceLocation getRandomLootTable(List<ResourceLocation> lootTables, Random random) {
        return lootTables.isEmpty() ? BuiltInLootTables.SIMPLE_DUNGEON : lootTables.get(random.nextInt(lootTables.size()));
    }

    public static BlockState getRandomBlock(List<Block> blocks, Random random) {
        return getRandomBlock(blocks, random, null);
    }

    public static BlockState getRandomBlock(List<Block> blocks, Random random, @Nullable Direction facing) {
        if (blocks.size() == 0)
            return Blocks.AIR.defaultBlockState();

        Block block = blocks.get(random.nextInt(blocks.size()));
        BlockState state = block.defaultBlockState();

        if (blocks == DECORATION_BLOCKS) {
            // sort out orientation
            if (facing != null) {
                if (block == Blocks.BLAST_FURNACE)
                    state = state.setValue(BlastFurnaceBlock.FACING, facing);
                if (block == Blocks.CARVED_PUMPKIN)
                    state = state.setValue(CarvedPumpkinBlock.FACING, facing);
                if (block == Blocks.DISPENSER)
                    state = state.setValue(DispenserBlock.FACING, facing);
                if (block == Blocks.FURNACE)
                    state = state.setValue(FurnaceBlock.FACING, facing);
                if (block == Blocks.LECTERN)
                    state = state.setValue(LecternBlock.FACING, facing);
                if (block == Blocks.OBSERVER)
                    state = state.setValue(ObserverBlock.FACING, facing);
                if (block == Blocks.SMOKER)
                    state = state.setValue(SmokerBlock.FACING, facing);
            }

            // some blocks have custom state, randomize it here
            if (block == Blocks.CAMPFIRE)
                state = state.setValue(CampfireBlock.LIT, false);
            if (block == Blocks.COMPOSTER)
                state = state.setValue(ComposterBlock.LEVEL, random.nextInt(7));
        }

        if (STATE_CALLBACK.containsKey(block))
            state = STATE_CALLBACK.get(block).apply(facing);

        return state;
    }

    public static IVariantMaterial getRandomVariantMaterial(Random random) {
        return VARIANT_MATERIALS.get(random.nextInt(VARIANT_MATERIALS.size()));
    }

    public static IVariantMaterial getRandomOverworldVariantMaterial(Random random) {
        return OVERWORLD_VARIANT_MATERIALS.get(random.nextInt(OVERWORLD_VARIANT_MATERIALS.size()));
    }

    @Nullable
    public static IVariantMaterial getVariantMaterial(String string) {
        for (IVariantMaterial material : VARIANT_MATERIALS) {
            if (material.getSerializedName().equals(string))
                return material;
        }

        return null;
    }
}
