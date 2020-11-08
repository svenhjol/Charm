package svenhjol.charm.base.helper;

import net.minecraft.block.*;
import net.minecraft.entity.EntityType;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Direction;
import svenhjol.charm.base.enums.IVariantMaterial;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Function;

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
    public static List<Identifier> RARE_CHEST_LOOT_TABLES = new ArrayList<>();
    public static List<Block> RARE_ORES = new ArrayList<>();
    public static List<Block> SAPLINGS = new ArrayList<>();
    public static List<EntityType<?>> SPAWNER_MOBS = new ArrayList<>();
    public static List<Block> STRIPPED_LOGS = new ArrayList<>();
    public static List<Block> STRIPPED_WOOD = new ArrayList<>();
    public static List<IVariantMaterial> VARIANT_MATERIALS = new ArrayList<>();
    public static List<Block> WOOL = new ArrayList<>();

    public static Map<Block, Function<Direction, BlockState>> STATE_CALLBACK = new HashMap<>();

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
            if (block == Blocks.COMPOSTER)
                state = state.with(ComposterBlock.LEVEL, random.nextInt(7));
        }

        if (STATE_CALLBACK.containsKey(block))
            state = STATE_CALLBACK.get(block).apply(facing);

        return state;
    }

    public static IVariantMaterial getRandomVariantMaterial(Random random) {
        return VARIANT_MATERIALS.get(random.nextInt(VARIANT_MATERIALS.size()));
    }
}
