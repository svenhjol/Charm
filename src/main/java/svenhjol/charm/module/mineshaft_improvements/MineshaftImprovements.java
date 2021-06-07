package svenhjol.charm.module.mineshaft_improvements;

import com.mojang.datafixers.util.Pair;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureFeatureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.MineshaftFeature;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.MineShaftPieces;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import svenhjol.charm.Charm;
import svenhjol.charm.annotation.Config;
import svenhjol.charm.annotation.Module;
import svenhjol.charm.handler.ModuleHandler;
import svenhjol.charm.mixin.accessor.MineshaftCorridorAccessor;
import svenhjol.charm.mixin.accessor.MineshaftPartAccessor;
import svenhjol.charm.mixin.accessor.StructurePieceAccessor;
import svenhjol.charm.module.CharmModule;
import svenhjol.charm.module.copper_rails.CopperRails;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Module(mod = Charm.MOD_ID, description = "Adds decoration and more ores to mineshafts.",
    requiresMixins = {"mineshaft_pieces.*"})
public class MineshaftImprovements extends CharmModule {
    public static List<BlockState> floorBlocks = new ArrayList<>();
    public static List<BlockState> ceilingBlocks = new ArrayList<>();
    public static List<Pair<BlockState, BlockState>> pileBlocks = new ArrayList<>();
    public static List<BlockState> roomBlocks = new ArrayList<>();
    public static List<BlockState> roomDecoration = new ArrayList<>();
    public static List<ResourceLocation> minecartLootTables = new ArrayList<>();

    public static float floorBlockChance = 0.019F;
    public static float ceilingBlockChance = 0.01F;
    public static float roomBlockChance = 0.25F;
    public static float blockPileChance = 0.2F;
    public static float minecartChance = 0.15F;

    private static boolean isEnabled = false;

    @Config(name = "Corridor blocks", description = "If true, lanterns and TNT will spawn inside mineshaft corridors.")
    public static boolean generateCorridorBlocks = true;

    @Config(name = "Corridor block piles", description = "If true, occasionally there will be piles of ore in mineshaft corridors.")
    public static boolean generateCorridorPiles = true;

    @Config(name = "Room blocks", description = "If true, precious ores will spawn in the central mineshaft room.")
    public static boolean generateRoomBlocks = true;

    @Config(name = "Add minecarts", description = "If true, minecarts of different kinds will be added to mineshaft corridors.")
    public static boolean generateMinecarts = true;

    @Override
    public void init() {
        isEnabled = true;

        floorBlocks.addAll(Arrays.asList(
            Blocks.TNT.defaultBlockState(),
            Blocks.IRON_ORE.defaultBlockState(),
            Blocks.GOLD_ORE.defaultBlockState(),
            Blocks.COPPER_ORE.defaultBlockState(),
            Blocks.LANTERN.defaultBlockState(),
            Blocks.CAMPFIRE.defaultBlockState().setValue(CampfireBlock.LIT, false)
        ));

        pileBlocks.addAll(Arrays.asList(
            new Pair<>(Blocks.IRON_ORE.defaultBlockState(), Blocks.RAW_IRON_BLOCK.defaultBlockState()),
            new Pair<>(Blocks.COPPER_ORE.defaultBlockState(), Blocks.RAW_COPPER_BLOCK.defaultBlockState()),
            new Pair<>(Blocks.GOLD_ORE.defaultBlockState(), Blocks.RAW_GOLD_BLOCK.defaultBlockState()),
            new Pair<>(Blocks.LAPIS_ORE.defaultBlockState(), Blocks.COAL_ORE.defaultBlockState()),
            new Pair<>(Blocks.REDSTONE_ORE.defaultBlockState(), Blocks.COBBLESTONE.defaultBlockState()),
            new Pair<>(Blocks.GRAVEL.defaultBlockState(), Blocks.COAL_ORE.defaultBlockState()),
            new Pair<>(Blocks.COARSE_DIRT.defaultBlockState(), Blocks.DIRT.defaultBlockState()),
            new Pair<>(Blocks.DEEPSLATE.defaultBlockState(), Blocks.DEEPSLATE_COAL_ORE.defaultBlockState()),
            new Pair<>(Blocks.DEEPSLATE.defaultBlockState(), Blocks.DEEPSLATE_IRON_ORE.defaultBlockState()),
            new Pair<>(Blocks.DEEPSLATE.defaultBlockState(), Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState())
        ));

        ceilingBlocks.addAll(Arrays.asList(
            Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true),
            Blocks.CHAIN.defaultBlockState().setValue(ChainBlock.AXIS, Direction.Axis.Y)
        ));

        roomBlocks.addAll(Arrays.asList(
            Blocks.DIAMOND_ORE.defaultBlockState(),
            Blocks.EMERALD_ORE.defaultBlockState(),
            Blocks.GOLD_ORE.defaultBlockState(),
            Blocks.LAPIS_ORE.defaultBlockState(),
            Blocks.DEEPSLATE_DIAMOND_ORE.defaultBlockState(),
            Blocks.DEEPSLATE_EMERALD_ORE.defaultBlockState(),
            Blocks.DEEPSLATE_GOLD_ORE.defaultBlockState(),
            Blocks.DEEPSLATE_LAPIS_ORE.defaultBlockState()
        ));

        roomDecoration.addAll(Arrays.asList(
            Blocks.MOSSY_COBBLESTONE.defaultBlockState(),
            Blocks.MOSSY_COBBLESTONE_SLAB.defaultBlockState(),
            Blocks.DIRT.defaultBlockState()
        ));

        minecartLootTables.addAll(Arrays.asList(
            BuiltInLootTables.SIMPLE_DUNGEON,
            BuiltInLootTables.ABANDONED_MINESHAFT,
            BuiltInLootTables.VILLAGE_TEMPLE,
            BuiltInLootTables.VILLAGE_CARTOGRAPHER,
            BuiltInLootTables.VILLAGE_MASON,
            BuiltInLootTables.VILLAGE_TOOLSMITH,
            BuiltInLootTables.VILLAGE_WEAPONSMITH
        ));

        // add candles to floor blocks
        List<Block> candles = new ArrayList<>(Arrays.asList(
            Blocks.BLACK_CANDLE, Blocks.BROWN_CANDLE, Blocks.GRAY_CANDLE
        ));

        candles.forEach(candle -> {
            for (int i = 1; i <= 4; i++) {
                floorBlocks.add(candle.defaultBlockState().setValue(CandleBlock.LIT, true).setValue(CandleBlock.CANDLES, i));
            }
        });

        if (ModuleHandler.enabled("charm:copper_rails")) {
            floorBlocks.add(CopperRails.COPPER_RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.EAST_WEST));
            floorBlocks.add(CopperRails.COPPER_RAIL.defaultBlockState().setValue(RailBlock.SHAPE, RailShape.NORTH_SOUTH));
        }
    }

    public static void generatePiece(StructurePiece piece, WorldGenLevel world, StructureFeatureManager accessor, ChunkGenerator chunkGenerator, Random rand, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (!isEnabled)
            return;

        // don't add any decoration to mesa mineshafts
        if (((MineshaftPartAccessor)piece).getType() == MineshaftFeature.Type.MESA)
            return;

        if (piece instanceof MineShaftPieces.MineShaftCorridor) {
            corridor((MineShaftPieces.MineShaftCorridor)piece, world, accessor, chunkGenerator, rand, box, chunkPos, blockPos);
        } else if (piece instanceof MineShaftPieces.MineShaftRoom) {
            room((MineShaftPieces.MineShaftRoom)piece, world, accessor, chunkGenerator, rand, box, chunkPos, blockPos);
        }
    }

    private static void corridor(MineShaftPieces.MineShaftCorridor piece, WorldGenLevel world, StructureFeatureManager accessor, ChunkGenerator chunkGenerator, Random rand, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (generateCorridorBlocks) {
            for (int x = 0; x < 3; x++) {
                if (x == 1 && rand.nextFloat() < 0.08F)
                    continue; // rarely, spawn some block in the middle of the corridor
                for (int z = 0; z < 7; z++) {
                    boolean validCeiling = ((MineshaftPartAccessor) piece).invokeIsSupportingBox(world, box, x, x, 2, z);
                    boolean validFloor = validFloorBlock(piece, world, x, 0, z, box);

                    if (!validCeiling)
                        continue;

                    if (validFloor && rand.nextFloat() < floorBlockChance && ((MineshaftCorridorAccessor)piece).invokeHasSturdyNeighbours(world, box, x, 0, z, 2)) {
                        ((StructurePieceAccessor)piece).invokePlaceBlock(world, getFloorBlock(rand), x, 0, z, box);
                    } else if (rand.nextFloat() < ceilingBlockChance && ((MineshaftCorridorAccessor)piece).invokeHasSturdyNeighbours(world, box, x, 2, z, 2)) {
                        ((StructurePieceAccessor)piece).invokePlaceBlock(world, getCeilingBlock(rand), x, 2, z, box);
                    }
                }
            }
        }

        if (generateCorridorPiles && rand.nextFloat() < blockPileChance) {
            int z = rand.nextInt(7);
            if (validFloorBlock(piece, world, 1, 0, z, box)) {

                // select two block states to render in the pile
                Pair<BlockState, BlockState> pair = getRandomPairFromList(pileBlocks, rand);

                for (int iy = 0; iy < 3; iy++) {
                    for (int ix = 0; ix <= 2; ix++) {
                        for (int iz = -1; iz <= 1; iz++) {
                            boolean valid = validFloorBlock(piece, world, ix, iy, iz, box);
                            if (valid && rand.nextFloat() < 0.7F)
                                ((StructurePieceAccessor)piece).invokePlaceBlock(world, rand.nextFloat() < 0.5 ? pair.getFirst() : pair.getSecond(), ix, iy, iz, box);
                        }
                    }
                }
            }
        }

        if (generateMinecarts && rand.nextFloat() < minecartChance) {
            int y = ((StructurePieceAccessor)piece).invokeGetWorldY(0);
            int x = ((StructurePieceAccessor)piece).invokeGetWorldX(1, 0);
            int z = ((StructurePieceAccessor)piece).invokeGetWorldZ(1, 0);

            BlockPos cartPos = new BlockPos(x, y, z);
            ResourceLocation loot = minecartLootTables.get(rand.nextInt(minecartLootTables.size()));

            if (box.isInside(cartPos) && world.getBlockState(cartPos).isAir() && !world.getBlockState(cartPos.below()).isAir()) {
                BlockState blockState = Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, rand.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
                ((StructurePieceAccessor)piece).invokePlaceBlock(world, blockState, x, y, z, box);

                AbstractMinecart minecartEntity;
                ServerLevel serverWorld = world.getLevel();
                double cartX = (double) cartPos.getX() + 0.5D;
                double cartY = (double) cartPos.getY() + 0.5D;
                double cartZ = (double) cartPos.getZ() + 0.5D;

                if (rand.nextFloat() < 0.4F) {
                    minecartEntity = new MinecartChest(serverWorld, cartX, cartY, cartZ);
                    ((MinecartChest)minecartEntity).setLootTable(loot, rand.nextLong());
                } else if (rand.nextFloat() < 0.4F) {
                    minecartEntity = new MinecartTNT(serverWorld, cartX, cartY, cartZ);
                } else if (rand.nextFloat() < 0.4F) {
                    minecartEntity = new MinecartHopper(serverWorld, cartX, cartY, cartZ);
                } else if (rand.nextFloat() < 0.4F) {
                    minecartEntity = new MinecartFurnace(serverWorld, cartX, cartY, cartZ);
                } else {
                    minecartEntity = new Minecart(serverWorld, cartX, cartY, cartZ);
                }

                world.addFreshEntity(minecartEntity);
            }
        }
    }

    private static void room(MineShaftPieces.MineShaftRoom piece, WorldGenLevel world, StructureFeatureManager accessor, ChunkGenerator chunkGenerator, Random rand, BoundingBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (generateRoomBlocks) {
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
                                state = rand.nextFloat() < 0.5F
                                    ? getRandomFromList(roomBlocks, rand)
                                    : getRandomFromList(roomDecoration, rand);
                            } else {
                                if (rand.nextFloat() < 0.5F) continue;
                                state = getRandomFromList(roomBlocks, rand);
                            }
                            BlockPos pos = new BlockPos(((StructurePieceAccessor)piece).getBoundingBox().minX() + x, ((StructurePieceAccessor)piece).getBoundingBox().minY() + y, ((StructurePieceAccessor)piece).getBoundingBox().minZ() + z);

                            if (world.isEmptyBlock(pos)
                                && world.getBlockState(pos.below()).isCollisionShapeFullBlock(world, pos.below())
                                && !world.canSeeSkyFromBelowWater(pos)) {
                                world.setBlock(pos, state, 11);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean validFloorBlock(StructurePiece piece, WorldGenLevel world, int x, int y, int z, BoundingBox box) {
        BlockPos blockpos = new BlockPos(
            ((StructurePieceAccessor)piece).invokeGetWorldX(x, z),
            ((StructurePieceAccessor)piece).invokeGetWorldY(y),
            ((StructurePieceAccessor)piece).invokeGetWorldZ(x, z)
        );

        boolean vecInside = box.isInside(blockpos);
        boolean solidBelow = world.getBlockState(blockpos.below()).canOcclude();
        boolean notSlabBelow = !(world.getBlockState(blockpos.below()).getBlock() instanceof SlabBlock);
        boolean airAbove = world.isEmptyBlock(blockpos.above());
        return vecInside
            && solidBelow
            && notSlabBelow
            && airAbove;
    }

    private static BlockState getFloorBlock(Random rand) {
        return getRandomFromList(floorBlocks, rand);
    }

    private static BlockState getCeilingBlock(Random rand) {
        return getRandomFromList(ceilingBlocks, rand);
    }

    private static BlockState getRandomFromList(List<BlockState> blocks, Random rand) {
        return blocks.get(rand.nextInt(blocks.size()));
    }

    private static Pair<BlockState, BlockState> getRandomPairFromList(List<Pair<BlockState, BlockState>> blocks, Random rand) {
        return blocks.get(rand.nextInt(blocks.size()));
    }
}
