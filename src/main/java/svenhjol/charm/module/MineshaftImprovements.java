package svenhjol.charm.module;

import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.LanternBlock;
import net.minecraft.block.SlabBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.loot.LootTables;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.structure.MineshaftGenerator.MineshaftCorridor;
import net.minecraft.structure.MineshaftGenerator.MineshaftRoom;
import net.minecraft.structure.StructurePiece;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.StructureWorldAccess;
import net.minecraft.world.gen.StructureAccessor;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.MineshaftFeature;
import svenhjol.charm.Charm;
import svenhjol.charm.base.handler.ModuleHandler;
import svenhjol.charm.blockentity.CrateBlockEntity;
import svenhjol.charm.base.CharmModule;
import svenhjol.charm.base.iface.Config;
import svenhjol.charm.base.iface.Module;
import svenhjol.charm.mixin.accessor.MineshaftGeneratorAccessor;
import svenhjol.charm.mixin.accessor.StructurePieceAccessor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Module(mod = Charm.MOD_ID, description = "Adds decoration and more ores to mineshafts.")
public class MineshaftImprovements extends CharmModule {
    public static List<BlockState> commonFloorBlocks = new ArrayList<>();
    public static List<BlockState> commonCeilingBlocks = new ArrayList<>();
    public static List<BlockState> rareFloorBlocks = new ArrayList<>();
    public static List<BlockState> rareCeilingBlocks = new ArrayList<>();
    public static List<BlockState> pileBlocks = new ArrayList<>();
    public static List<BlockState> roomBlocks = new ArrayList<>();
    public static List<BlockState> roomDecoration = new ArrayList<>();
    public static List<Identifier> crateLootTables = new ArrayList<>();

    public static float floorBlockChance = 0.04F;
    public static float ceilingBlockChance = 0.02F;
    public static float rareBlockChance = 0.08F;
    public static float roomBlockChance = 0.3F;
    public static float blockPileChance = 0.1F;
    public static float crateChance = 0.1F;

    private static boolean isEnabled = false;

    @Config(name = "Corridor blocks", description = "If true, stone, ore, lanterns and TNT will spawn inside mineshaft corridors.")
    public static boolean generateCorridorBlocks = true;

    @Config(name = "Corridor block piles", description = "If true, occasionally there will be piles of ore in mineshaft corridors.")
    public static boolean generateCorridorPiles = true;

    @Config(name = "Room blocks", description = "If true, precious ores will spawn in the central mineshaft room.")
    public static boolean generateRoomBlocks = true;

    @Config(name = "Add crates", description = "If true, loot crates will be added to mineshaft corridors.")
    public static boolean generateCrates = true;

    @Override
    public void init() {
        isEnabled = true;

        commonFloorBlocks.addAll(Arrays.asList(
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.COAL_ORE.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState()
        ));

        rareFloorBlocks.addAll(Arrays.asList(
            Blocks.TNT.getDefaultState(),
            Blocks.LANTERN.getDefaultState()
        ));

        pileBlocks.addAll(Arrays.asList(
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.COAL_ORE.getDefaultState(),
            Blocks.REDSTONE_ORE.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.COBBLESTONE.getDefaultState(),
            Blocks.COARSE_DIRT.getDefaultState(),
            Blocks.GRAVEL.getDefaultState()
        ));

        commonCeilingBlocks.addAll(Arrays.asList(
            Blocks.COBWEB.getDefaultState(),
            Blocks.LANTERN.getDefaultState().with(LanternBlock.HANGING, true),
            Blocks.IRON_ORE.getDefaultState(),
            Blocks.COAL_ORE.getDefaultState()
        ));

        rareCeilingBlocks.addAll(Arrays.asList(
            Blocks.GOLD_ORE.getDefaultState()
        ));

        roomBlocks.addAll(Arrays.asList(
            Blocks.DIAMOND_ORE.getDefaultState(),
            Blocks.GOLD_ORE.getDefaultState(),
            Blocks.LAPIS_ORE.getDefaultState()
        ));

        roomDecoration.addAll(Arrays.asList(
            Blocks.MOSSY_COBBLESTONE.getDefaultState(),
            Blocks.MOSSY_COBBLESTONE_SLAB.getDefaultState(),
            Blocks.DIRT.getDefaultState()
        ));

        crateLootTables.addAll(Arrays.asList(
            LootTables.ABANDONED_MINESHAFT_CHEST,
            LootTables.SIMPLE_DUNGEON_CHEST,
            LootTables.VILLAGE_CARTOGRAPHER_CHEST,
            LootTables.VILLAGE_MASON_CHEST,
            LootTables.VILLAGE_TOOLSMITH_CHEST,
            LootTables.VILLAGE_WEAPONSMITH_CHEST
        ));
    }

    public static void generatePiece(StructurePiece piece, StructureWorldAccess world, StructureAccessor accessor, ChunkGenerator chunkGenerator, Random rand, BlockBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (!isEnabled)
            return;

        if (((MineshaftGeneratorAccessor)piece).getMineshaftType() == MineshaftFeature.Type.MESA)
            return;

        if (piece instanceof MineshaftCorridor) {
            corridor((MineshaftCorridor)piece, world, accessor, chunkGenerator, rand, box, chunkPos, blockPos);
        } else if (piece instanceof MineshaftRoom) {
            room((MineshaftRoom)piece, world, accessor, chunkGenerator, rand, box, chunkPos, blockPos);
        }
    }

    private static void corridor(MineshaftCorridor piece, StructureWorldAccess world, StructureAccessor accessor, ChunkGenerator chunkGenerator, Random rand, BlockBox box, ChunkPos chunkPos, BlockPos blockPos) {
        int bx = box.maxX - box.minX;
        int bz = box.maxZ - box.minZ;

        if (generateCorridorBlocks) {
            if (bx <= 0) bx = 3;
            if (bz <= 0) bz = 7;
            for (int x = 0; x < bx; x++) {
                if (x == 1 && rand.nextFloat() < 0.08F)
                    continue; // rarely, spawn some block in the middle of the corridor
                for (int z = 0; z < bz; z++) {
                    if (validFloorBlock(piece, world, x, 0, z, box) && rand.nextFloat() < floorBlockChance) {
                        ((StructurePieceAccessor)piece).callAddBlock(world, getFloorBlock(rand), x, 0, z, box);
                    }
                    if (validCeilingBlock(piece, world, x, 2, z, box) && rand.nextFloat() < ceilingBlockChance) {
                        ((StructurePieceAccessor)piece).callAddBlock(world, getCeilingBlock(rand), x, 2, z, box);
                    }
                }
            }
        }

        if (generateCorridorPiles && rand.nextFloat() < blockPileChance) {
            int z = rand.nextInt(bz);
            if (validFloorBlock(piece, world, 1, 0, z, box)) {

                // select two block states to render in the pile
                BlockState block1 = getRandomBlockFromList(pileBlocks, rand);
                BlockState block2 = getRandomBlockFromList(pileBlocks, rand);

                for (int iy = 0; iy < 3; iy++) {
                    for (int ix = 0; ix <= 2; ix++) {
                        for (int iz = -1; iz <= 1; iz++) {
                            boolean valid = validFloorBlock(piece, world, ix, iy, iz, box);
                            if (valid && rand.nextFloat() < 0.75F)
                                ((StructurePieceAccessor)piece).callAddBlock(world, rand.nextFloat() < 0.5 ? block1 : block2, ix, iy, iz, box);
                        }
                    }
                }
            }
        }

        if (generateCrates && ModuleHandler.enabled("charm:crates") && rand.nextFloat() < crateChance) {
            if (rand.nextFloat() < 0.9F) {
                int r = rand.nextInt(3) + 12;
                int y = ((StructurePieceAccessor)piece).callApplyYTransform(0);
                int x = ((StructurePieceAccessor)piece).callApplyXTransform(1, r);
                int z = ((StructurePieceAccessor)piece).callApplyZTransform(1, r);

                BlockPos blockpos = new BlockPos(x, y, z);

                if (box.contains(blockpos)) {
                    BlockState state = Crates.getRandomCrateBlock(rand).getDefaultState();
                    Identifier loot = crateLootTables.get(rand.nextInt(crateLootTables.size()));

                    world.setBlockState(blockpos, state, 2);

                    BlockEntity blockEntity = world.getBlockEntity(blockpos);
                    if (blockEntity instanceof CrateBlockEntity) {
                        ((CrateBlockEntity) blockEntity).setLootTable(loot, rand.nextLong());
                        blockEntity.toTag(new CompoundTag());
                    }
                }
            }
        }
    }

    private static void room(MineshaftRoom piece, ServerWorldAccess world, StructureAccessor accessor, ChunkGenerator chunkGenerator, Random rand, BlockBox box, ChunkPos chunkPos, BlockPos blockPos) {
        if (generateRoomBlocks) {
            int bx = box.maxX - box.minX;
            int bz = box.maxZ - box.minZ;

            if (bx <= 0) bx = 15;
            if (bz <= 0) bz = 15;

            for (int y = 1; y <= 2; y++) {
                for (int x = 0; x <= bx; x++) {
                    for (int z = 0; z <= bz; z++) {
                        if (rand.nextFloat() < roomBlockChance) {
                            BlockState state;
                            if (y == 1) {
                                state = rand.nextFloat() < 0.5F
                                    ? getRandomBlockFromList(roomBlocks, rand)
                                    : getRandomBlockFromList(roomDecoration, rand);
                            } else {
                                if (rand.nextFloat() < 0.5F) continue;
                                state = getRandomBlockFromList(roomBlocks, rand);
                            }
                            BlockPos pos = new BlockPos(((StructurePieceAccessor)piece).getBoundingBox().minX + x, ((StructurePieceAccessor)piece).getBoundingBox().minY + y, ((StructurePieceAccessor)piece).getBoundingBox().minZ + z);

                            if (world.isAir(pos)
                                && world.getBlockState(pos.down()).isFullCube(world, pos.down())
                                && !world.isSkyVisibleAllowingSea(pos)) {
                                world.setBlockState(pos, state, 11);
                            }
                        }
                    }
                }
            }
        }
    }

    private static boolean validCeilingBlock(StructurePiece piece, ServerWorldAccess world, int x, int y, int z, BlockBox box) {
        BlockPos blockpos = new BlockPos(
            ((StructurePieceAccessor)piece).callApplyXTransform(x, z),
            ((StructurePieceAccessor)piece).callApplyYTransform(y),
            ((StructurePieceAccessor)piece).callApplyZTransform(x, z));
        return box.contains(blockpos)
            && world.getBlockState(blockpos.up()).isOpaque()
            && world.isAir(blockpos.down());
    }

    private static boolean validFloorBlock(StructurePiece piece, ServerWorldAccess world, int x, int y, int z, BlockBox box) {
        BlockPos blockpos = new BlockPos(
            ((StructurePieceAccessor)piece).callApplyXTransform(x, z),
            ((StructurePieceAccessor)piece).callApplyYTransform(y),
            ((StructurePieceAccessor)piece).callApplyZTransform(x, z)
        );

        boolean vecInside = box.contains(blockpos);
        boolean solidBelow = world.getBlockState(blockpos.down()).isOpaque();
        boolean notSlabBelow = !(world.getBlockState(blockpos.down()).getBlock() instanceof SlabBlock);
        boolean airAbove = world.isAir(blockpos.up());
        return vecInside
            && solidBelow
            && notSlabBelow
            && airAbove;
    }

    private static BlockState getFloorBlock(Random rand) {
        return rand.nextFloat() < rareBlockChance
            ? getRandomBlockFromList(rareFloorBlocks, rand)
            : getRandomBlockFromList(commonFloorBlocks, rand);
    }

    private static BlockState getCeilingBlock(Random rand) {
        return rand.nextFloat() < rareBlockChance
            ? getRandomBlockFromList(rareCeilingBlocks, rand)
            : getRandomBlockFromList(commonCeilingBlocks, rand);
    }

    private static BlockState getRandomBlockFromList(List<BlockState> blocks, Random rand) {
        return blocks.get(rand.nextInt(blocks.size()));
    }
}
