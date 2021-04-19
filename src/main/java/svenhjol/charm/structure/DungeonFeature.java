package svenhjol.charm.structure;

import com.mojang.serialization.Codec;
import net.minecraft.block.BlockState;
import net.minecraft.structure.MarginedStructureStart;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.StructurePool;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePoolElement;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.BlockRotation;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.EmptyBlockView;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.VerticalBlockSample;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

import java.util.Random;

public class DungeonFeature extends StructureFeature<StructurePoolFeatureConfig> {
    public static final int DEFAULT_Y = 2;

    public DungeonFeature(Codec<StructurePoolFeatureConfig> codec) {
        super(codec);
    }

    public StructureFeature.StructureStartFactory<StructurePoolFeatureConfig> getStructureStartFactory() {
        return (feature, chunkPos, i, l) -> new Start(this, chunkPos, i, l);
    }

    public static class Start extends MarginedStructureStart<StructurePoolFeatureConfig> {
        public Start(DungeonFeature feature, ChunkPos chunkPos, int i, long l) {
            super(feature, chunkPos, i, l);
        }

        public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, StructurePoolFeatureConfig structurePoolFeatureConfig, HeightLimitView heightLimitView) {
            int x = chunkPos.getStartX();
            int y = DEFAULT_Y + new Random().nextInt(32) + 8;
            int z = chunkPos.getStartZ();
            boolean found = false;
            BlockPos.Mutable blockPos = new BlockPos.Mutable(x, y, z);

            Random random = new Random(blockPos.asLong());
            Random random1 = new Random(blockPos.asLong()); // this gets passed to the generator and must be the same seed as the one used here.

            BlockRotation blockRotation = BlockRotation.random(random);
            StructurePool structurePool = structurePoolFeatureConfig.getStartPool().get();
            StructurePoolElement structurePoolElement = structurePool.getRandomElement(new Random());
            BlockBox box = structurePoolElement.getBoundingBox(structureManager, blockPos, blockRotation);

            int[] corner1 = new int[]{box.getMinX(), box.getMinZ()};
            int[] corner2 = new int[]{box.getMaxX(), box.getMinZ()};
            int[] corner3 = new int[]{box.getMinX(), box.getMaxZ()};
            int[] corner4 = new int[]{box.getMaxX(), box.getMaxZ()};

            VerticalBlockSample cornerSample1 = chunkGenerator.getColumnSample(corner1[0], corner1[1], heightLimitView);
            VerticalBlockSample cornerSample2 = chunkGenerator.getColumnSample(corner2[0], corner2[1], heightLimitView);
            VerticalBlockSample cornerSample3 = chunkGenerator.getColumnSample(corner3[0], corner3[1], heightLimitView);
            VerticalBlockSample cornerSample4 = chunkGenerator.getColumnSample(corner4[0], corner4[1], heightLimitView);

            while (y > DEFAULT_Y && !found) {
                BlockPos.Mutable mutable = new BlockPos.Mutable(box.getMinX(), y, box.getMinZ());
                BlockState blockState = cornerSample1.getState(mutable);
                y--;

                if (blockState.isAir()) {
                    BlockPos pos1 = new BlockPos(corner1[0], y-1, corner1[1]);
                    BlockPos pos2 = new BlockPos(corner2[0], y-1, corner2[1]);
                    BlockPos pos3 = new BlockPos(corner3[0], y-1, corner3[1]);
                    BlockPos pos4 = new BlockPos(corner4[0], y-1, corner4[1]);

                    BlockState state1 = cornerSample1.getState(pos1);
                    BlockState state2 = cornerSample2.getState(pos2);
                    BlockState state3 = cornerSample3.getState(pos3);
                    BlockState state4 = cornerSample4.getState(pos4);

                    if (state1.isSolidBlock(EmptyBlockView.INSTANCE, pos1)
                        && state2.isSolidBlock(EmptyBlockView.INSTANCE, pos2)
                        && state3.isSolidBlock(EmptyBlockView.INSTANCE, pos3)
                        && state4.isSolidBlock(EmptyBlockView.INSTANCE, pos4)) {
                        found = true;
                    }
                }
            }

            if (found) {
                BlockPos foundPos = new BlockPos(x, y + 1, z);
                StructurePools.initDefaultPools();
                StructurePoolBasedGenerator.method_30419(dynamicRegistryManager, structurePoolFeatureConfig, PoolStructurePiece::new, chunkGenerator, structureManager, foundPos, this, random1, true, false, heightLimitView);
                this.setBoundingBoxFromChildren();
            }
        }
    }
}
