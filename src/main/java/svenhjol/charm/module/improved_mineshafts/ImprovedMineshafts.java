package svenhjol.charm.module.improved_mineshafts;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.MineshaftFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.MineShaftPieces;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.CommonModule;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.helper.ItemHelper;
import svenhjol.charm.loader.CharmModule;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@CommonModule(mod = Charm.MOD_ID, description = "Adds decoration and more ores to mineshafts.")
public class ImprovedMineshafts extends CharmModule {
    public static List<ResourceLocation> MINECART_LOOT_TABLES = new ArrayList<>();

    public static List<BlockState> FLOOR_BLOCKS = new ArrayList<>();
    public static List<BlockState> CEILING_BLOCKS = new ArrayList<>();
    public static List<BlockState> PILE_BLOCKS = new ArrayList<>();
    public static List<BlockState> ROOM_BLOCKS = new ArrayList<>();
    public static List<BlockState> ROOM_DECORATIONS = new ArrayList<>();

    public static ResourceLocation FLOOR_BLOCK_LOOT;
    public static ResourceLocation PILE_BLOCK_LOOT;
    public static ResourceLocation CEILING_BLOCK_LOOT;
    public static ResourceLocation ROOM_BLOCK_LOOT;
    public static ResourceLocation ROOM_DECORATION_LOOT;

    @Config(name = "Corridor floor blocks", description = "Chance (out of 1.0) of blocks such as candles and ores spawning on the floor of corridors.")
    public static double floorBlockChance = 0.03D;

    @Config(name = "Corridor ceiling blocks", description = "Chance (out of 1.0) of blocks such as lanterns spawning on the ceiling of corridors.")
    public static double ceilingBlockChance = 0.02D;

    @Config(name = "Corridor block piles", description = "Chance (out of 1.0) of stone, gravel and ore spawning at the entrance of corridors.")
    public static double blockPileChance = 0.2D;

    @Config(name = "Room blocks", description = "Chance (out of 1.0) for a moss or precious ore block to spawn on a single block of the central mineshaft room.")
    public static double roomBlockChance = 0.25D;

    @Config(name = "Extra minecarts", description = "Chance (out of 1.0) for a minecart to spawn in a corridor. Minecart loot is chosen from the 'Minecart loot tables'.")
    public static double minecartChance = 0.2D;

    @Config(name = "Minecart loot tables", description = "If 'Add minecarts' is enabled, minecarts have a chance to be filled with loot from these loot tables.")
    public static List<String> configMinecartLootTables;

    @Override
    public void runWhenEnabled() {
        ServerWorldEvents.LOAD.register(this::handleWorldLoad);
        configMinecartLootTables.forEach(tableId -> MINECART_LOOT_TABLES.add(new ResourceLocation(tableId)));
    }

    private void handleWorldLoad(MinecraftServer server, ServerLevel level) {
        if (level.dimension() == Level.OVERWORLD) {
            LootContext.Builder builder = new LootContext.Builder(level);

            FLOOR_BLOCKS.addAll(parseLootTable(server, builder, FLOOR_BLOCK_LOOT));
            PILE_BLOCKS.addAll(parseLootTable(server, builder, PILE_BLOCK_LOOT));
            CEILING_BLOCKS.addAll(parseLootTable(server, builder, CEILING_BLOCK_LOOT));
            ROOM_BLOCKS.addAll(parseLootTable(server, builder, ROOM_BLOCK_LOOT));
            ROOM_DECORATIONS.addAll(parseLootTable(server, builder, ROOM_DECORATION_LOOT));
        }
    }

    private List<BlockState> parseLootTable(MinecraftServer server, LootContext.Builder builder, ResourceLocation lootTable) {
        List<ItemStack> items = server.getLootTables().get(lootTable).getRandomItems(builder.create(LootContextParamSets.EMPTY));
        List<BlockState> states = new ArrayList<>();
        for (ItemStack stack : items) {
            BlockState state = ItemHelper.getBlockStateFromItemStack(stack);
            states.add(state);
        }
        return states;
    }

    public static void generatePiece(StructurePiece piece, WorldGenLevel level, StructureFeatureManager accessor, ChunkGenerator chunkGenerator, Random rand, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (!Charm.LOADER.isEnabled(ImprovedMineshafts.class))

        // don't add any decoration to mesa mineshafts
        if (((MineShaftPieces.MineShaftPiece)piece).type == MineshaftFeature.Type.MESA) return;

        if (piece instanceof MineShaftPieces.MineShaftCorridor) {
            corridor((MineShaftPieces.MineShaftCorridor)piece, level, accessor, chunkGenerator, rand, box, chunkPos, blockPos);
        } else if (piece instanceof MineShaftPieces.MineShaftRoom) {
            room((MineShaftPieces.MineShaftRoom)piece, level, accessor, chunkGenerator, rand, box, chunkPos, blockPos);
        }
    }

    private static void corridor(MineShaftPieces.MineShaftCorridor piece, WorldGenLevel level, StructureFeatureManager accessor, ChunkGenerator chunkGenerator, Random rand, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (floorBlockChance > 0 || ceilingBlockChance > 0) {
            for (int x = 0; x < 3; x++) {
                if (x == 1 && rand.nextFloat() < 0.08F)
                    continue; // rarely, spawn some block in the middle of the corridor
                for (int z = 0; z < 7; z++) {
                    boolean validCeiling = piece.isSupportingBox(level, box, x, x, 2, z);
                    boolean validFloor = validFloorBlock(piece, level, x, 0, z, box);

                    if (!validCeiling) continue;

                    if (validFloor && !FLOOR_BLOCKS.isEmpty() && rand.nextFloat() < floorBlockChance && piece.hasSturdyNeighbours(level, box, x, 0, z, 2)) {
                        BlockState state = getRandom(FLOOR_BLOCKS, rand);
                        piece.placeBlock(level, state, x, 0, z, box);
                    } else if (!CEILING_BLOCKS.isEmpty() && rand.nextFloat() < ceilingBlockChance && piece.hasSturdyNeighbours(level, box, x, 2, z, 2)) {
                        BlockState state = getRandom(CEILING_BLOCKS, rand);

                        // if the ceiling block is a chain then attach a hanging lantern to it
                        if (state.getBlock() == Blocks.CHAIN) {
                            piece.placeBlock(level, Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true), x, 1, z, box);
                        }

                        piece.placeBlock(level, state, x, 2, z, box);
                    }
                }
            }
        }

        if (PILE_BLOCKS.size() > 0 && rand.nextFloat() < blockPileChance) {
            int z = rand.nextInt(7);
            if (validFloorBlock(piece, level, 1, 0, z, box)) {
                BlockState state1 = getRandom(PILE_BLOCKS, rand);
                BlockState state2 = getRandom(PILE_BLOCKS, rand);

                for (int iy = 0; iy < 3; iy++) {
                    for (int ix = 0; ix <= 2; ix++) {
                        for (int iz = -1; iz <= 1; iz++) {
                            boolean valid = validFloorBlock(piece, level, ix, iy, iz, box);
                            if (valid && rand.nextFloat() < 0.7F) {
                                BlockState useState = rand.nextBoolean() ? state1 : state2;
                                piece.placeBlock(level, useState, ix, iy, iz, box);
                            }
                        }
                    }
                }
            }
        }

        if (!MINECART_LOOT_TABLES.isEmpty() && rand.nextFloat() < minecartChance) {
            int y = piece.getWorldY(0);
            int x = piece.getWorldX(1, 0);
            int z = piece.getWorldZ(1, 0);

            BlockPos cartPos = new BlockPos(x, y, z);
            ResourceLocation loot = MINECART_LOOT_TABLES.get(rand.nextInt(MINECART_LOOT_TABLES.size()));

            if (box.isInside(cartPos) && level.getBlockState(cartPos).isAir() && !level.getBlockState(cartPos.below()).isAir()) {
                BlockState blockState = Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, rand.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
                piece.placeBlock(level, blockState, x, y, z, box);

                AbstractMinecart minecart;
                ServerLevel serverWorld = level.getLevel();
                double cartX = (double) cartPos.getX() + 0.5D;
                double cartY = (double) cartPos.getY() + 0.5D;
                double cartZ = (double) cartPos.getZ() + 0.5D;

                if (rand.nextFloat() < 0.4F) {
                    minecart = new MinecartChest(serverWorld, cartX, cartY, cartZ);
                    ((MinecartChest)minecart).setLootTable(loot, rand.nextLong());
                } else if (rand.nextFloat() < 0.4F) {
                    minecart = new MinecartTNT(serverWorld, cartX, cartY, cartZ);
                } else if (rand.nextFloat() < 0.4F) {
                    minecart = new MinecartHopper(serverWorld, cartX, cartY, cartZ);
                } else if (rand.nextFloat() < 0.4F) {
                    minecart = new MinecartFurnace(serverWorld, cartX, cartY, cartZ);
                } else {
                    minecart = new Minecart(serverWorld, cartX, cartY, cartZ);
                }

                level.addFreshEntity(minecart);
            }
        }
    }

    private static void room(MineShaftPieces.MineShaftRoom piece, WorldGenLevel level, StructureFeatureManager accessor, ChunkGenerator chunkGenerator, Random rand, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (!ROOM_BLOCKS.isEmpty() && !ROOM_DECORATIONS.isEmpty()) {
            int bx = box.maxX() - box.minX();
            int bz = box.maxZ() - box.minZ();

            if (bx <= 0) bx = 15;
            if (bz <= 0) bz = 15;

            for (int y = 1; y <= 2; y++) {
                for (int x = 0; x <= bx; x++) {
                    for (int z = 0; z <= bz; z++) {
                        if (rand.nextFloat() < roomBlockChance) {
                            BlockState state;
                            if (y == 1) {
                                state = rand.nextFloat() < 0.5F ? getRandom(ROOM_BLOCKS, rand) : getRandom(ROOM_DECORATIONS, rand);
                            } else {
                                if (rand.nextFloat() < 0.5F) continue;
                                state = getRandom(ROOM_BLOCKS, rand);
                            }
                            BlockPos pos = new BlockPos(piece.getBoundingBox().minX() + x, piece.getBoundingBox().minY() + y, piece.getBoundingBox().minZ() + z);

                            if (level.isEmptyBlock(pos)
                                && level.getBlockState(pos.below()).isCollisionShapeFullBlock(level, pos.below())
                                && !level.canSeeSkyFromBelowWater(pos)) {
                                level.setBlock(pos, state, 11);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean validFloorBlock(StructurePiece piece, WorldGenLevel level, int x, int y, int z, BoundingBox box) {
        BlockPos blockpos = new BlockPos(
            piece.getWorldX(x, z),
            piece.getWorldY(y),
            piece.getWorldZ(x, z)
        );

        boolean vecInside = box.isInside(blockpos);
        boolean solidBelow = level.getBlockState(blockpos.below()).canOcclude();
        boolean notSlabBelow = !(level.getBlockState(blockpos.below()).getBlock() instanceof SlabBlock);
        boolean airAbove = level.isEmptyBlock(blockpos.above());
        return vecInside
            && solidBelow
            && notSlabBelow
            && airAbove;
    }

    private static BlockState getRandom(List<BlockState> blocks, Random rand) {
        if (blocks.isEmpty()) return Blocks.AIR.defaultBlockState();
        return blocks.get(rand.nextInt(blocks.size()));
    }

    static {
        configMinecartLootTables = Arrays.asList(
            BuiltInLootTables.SIMPLE_DUNGEON.getPath(),
            BuiltInLootTables.ABANDONED_MINESHAFT.getPath(),
            BuiltInLootTables.VILLAGE_TEMPLE.getPath(),
            BuiltInLootTables.VILLAGE_CARTOGRAPHER.getPath(),
            BuiltInLootTables.VILLAGE_MASON.getPath(),
            BuiltInLootTables.VILLAGE_TOOLSMITH.getPath(),
            BuiltInLootTables.VILLAGE_WEAPONSMITH.getPath()
        );

        FLOOR_BLOCK_LOOT = new ResourceLocation(Charm.MOD_ID, "improved_mineshafts/floor_blocks");
        PILE_BLOCK_LOOT = new ResourceLocation(Charm.MOD_ID, "improved_mineshafts/pile_blocks");
        CEILING_BLOCK_LOOT = new ResourceLocation(Charm.MOD_ID, "improved_mineshafts/ceiling_blocks");
        ROOM_BLOCK_LOOT = new ResourceLocation(Charm.MOD_ID, "improved_mineshafts/room_blocks");
        ROOM_DECORATION_LOOT = new ResourceLocation(Charm.MOD_ID, "improved_mineshafts/room_decorations");
    }
}
