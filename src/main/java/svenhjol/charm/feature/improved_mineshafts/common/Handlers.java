package svenhjol.charm.feature.improved_mineshafts.common;

import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.vehicle.*;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.LanternBlock;
import net.minecraft.world.level.block.RailBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.RailShape;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftPieces.MineShaftCorridor;
import net.minecraft.world.level.levelgen.structure.structures.MineshaftStructure;
import svenhjol.charm.feature.improved_mineshafts.ImprovedMineshafts;
import svenhjol.charm.foundation.feature.FeatureHolder;

import java.util.List;

public final class Handlers extends FeatureHolder<ImprovedMineshafts> {
    public Handlers(ImprovedMineshafts feature) {
        super(feature);
    }

    public void generatePiece(StructurePiece piece, WorldGenLevel level, RandomSource random, BoundingBox box) {
        // Don't add any decoration to mesa mineshafts.
        if (((MineshaftPieces.MineShaftPiece)piece).type == MineshaftStructure.Type.MESA) return;

        if (piece instanceof MineShaftCorridor) {
            decorateCorridor((MineShaftCorridor)piece, level, random, box);
        } else if (piece instanceof MineshaftPieces.MineShaftRoom) {
            decorateRoom((MineshaftPieces.MineShaftRoom)piece, level, random, box);
        }
    }

    public void decorateCorridor(MineShaftCorridor piece, WorldGenLevel level, RandomSource random, BoundingBox box) {
        var registers = feature().registers;

        if (ImprovedMineshafts.floorBlockChance > 0 || ImprovedMineshafts.ceilingBlockChance > 0) {
            for (var x = 0; x < 3; x++) {
                if (x == 1 && random.nextFloat() < 0.08F) {
                    continue; // Rarely, spawn some block in the middle of the corridor.
                }

                for (var z = 0; z < 7; z++) {
                    var validCeiling = piece.isSupportingBox(level, box, x, x, 2, z);
                    var validFloor = validFloorBlock(piece, level, x, 0, z, box);
                    if (!validCeiling) continue;

                    if (validFloor && !registers.blocksForFloor.isEmpty() && random.nextFloat() < ImprovedMineshafts.floorBlockChance && piece.hasSturdyNeighbours(level, box, x, 0, z, 2)) {
                        var state = getRandom(registers.blocksForFloor, random);
                        piece.placeBlock(level, state, x, 0, z, box);
                    } else if (!registers.blocksForCeiling.isEmpty() && random.nextFloat() < ImprovedMineshafts.ceilingBlockChance && piece.hasSturdyNeighbours(level, box, x, 2, z, 2)) {
                        var state = getRandom(registers.blocksForCeiling, random);

                        // if the ceiling block is a chain then attach a hanging lantern to it
                        if (state.getBlock() == Blocks.CHAIN) {
                            piece.placeBlock(level, Blocks.LANTERN.defaultBlockState().setValue(LanternBlock.HANGING, true), x, 1, z, box);
                        }

                        piece.placeBlock(level, state, x, 2, z, box);
                    }
                }
            }
        }

        if (!registers.blocksForPile.isEmpty() && random.nextFloat() < ImprovedMineshafts.blockPileChance) {
            var z = random.nextInt(7);
            if (validFloorBlock(piece, level, 1, 0, z, box)) {
                var state1 = getRandom(registers.blocksForPile, random);
                var state2 = getRandom(registers.blocksForPile, random);

                for (var iy = 0; iy < 3; iy++) {
                    for (var ix = 0; ix <= 2; ix++) {
                        for (var iz = -1; iz <= 1; iz++) {
                            var valid = validFloorBlock(piece, level, ix, iy, iz, box);
                            if (valid && random.nextFloat() < 0.7F) {
                                var useState = random.nextBoolean() ? state1 : state2;
                                piece.placeBlock(level, useState, ix, iy, iz, box);
                            }
                        }
                    }
                }
            }
        }

        if (!registers.minecartLoot.isEmpty() && random.nextFloat() < ImprovedMineshafts.minecartChance) {
            var y = piece.getWorldY(0);
            var x = piece.getWorldX(1, 0);
            var z = piece.getWorldZ(1, 0);
            var cartPos = new BlockPos(x, y, z);
            var loot = registers.minecartLoot
                .get(random.nextInt(registers.minecartLoot.size()));

            if (box.isInside(cartPos) && level.getBlockState(cartPos).isAir() && !level.getBlockState(cartPos.below()).isAir()) {
                var blockState = Blocks.RAIL.defaultBlockState().setValue(RailBlock.SHAPE, random.nextBoolean() ? RailShape.NORTH_SOUTH : RailShape.EAST_WEST);
                piece.placeBlock(level, blockState, x, y, z, box);

                AbstractMinecart minecart;
                var serverLevel = level.getLevel();
                var cartX = (double) cartPos.getX() + 0.5D;
                var cartY = (double) cartPos.getY() + 0.5D;
                var cartZ = (double) cartPos.getZ() + 0.5D;

                if (random.nextFloat() < 0.4F) {
                    minecart = new MinecartChest(serverLevel, cartX, cartY, cartZ);
                    ((MinecartChest)minecart).setLootTable(loot, random.nextLong());
                } else if (random.nextFloat() < 0.4F) {
                    minecart = new MinecartTNT(serverLevel, cartX, cartY, cartZ);
                } else if (random.nextFloat() < 0.4F) {
                    minecart = new MinecartFurnace(serverLevel, cartX, cartY, cartZ);
                } else {
                    minecart = new Minecart(serverLevel, cartX, cartY, cartZ);
                }

                level.addFreshEntity(minecart);
            }
        }
    }

    public void decorateRoom(MineshaftPieces.MineShaftRoom piece, WorldGenLevel level, RandomSource random, BoundingBox box) {
        var registers = feature().registers;

        if (!registers.blocksForRoom.isEmpty() && !registers.decorationsForRoom.isEmpty()) {
            var bx = box.maxX() - box.minX();
            var bz = box.maxZ() - box.minZ();

            if (bx <= 0) bx = 15;
            if (bz <= 0) bz = 15;

            for (int y = 1; y <= 2; y++) {
                for (int x = 0; x <= bx; x++) {
                    for (int z = 0; z <= bz; z++) {
                        if (random.nextFloat() < ImprovedMineshafts.roomBlockChance) {
                            BlockState state;

                            if (y == 1) {
                                state = random.nextFloat() < 0.5F ? getRandom(registers.blocksForRoom, random) : getRandom(registers.decorationsForRoom, random);
                            } else {
                                if (random.nextFloat() < 0.5F) continue;
                                state = getRandom(registers.blocksForRoom, random);
                            }

                            var pos = new BlockPos(piece.getBoundingBox().minX() + x, piece.getBoundingBox().minY() + y, piece.getBoundingBox().minZ() + z);
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

    public boolean validFloorBlock(StructurePiece piece, WorldGenLevel level, int x, int y, int z, BoundingBox box) {
        var blockpos = new BlockPos(
            piece.getWorldX(x, z),
            piece.getWorldY(y),
            piece.getWorldZ(x, z)
        );

        var vecInside = box.isInside(blockpos);
        var solidBelow = level.getBlockState(blockpos.below()).canOcclude();
        var notSlabBelow = !(level.getBlockState(blockpos.below()).getBlock() instanceof SlabBlock);
        var airAbove = level.isEmptyBlock(blockpos.above());

        return vecInside
               && solidBelow
               && notSlabBelow
               && airAbove;
    }

    public BlockState getRandom(List<BlockState> blocks, RandomSource random) {
        if (blocks.isEmpty()) {
            return Blocks.AIR.defaultBlockState();
        }

        return blocks.get(random.nextInt(blocks.size()));
    }
}
