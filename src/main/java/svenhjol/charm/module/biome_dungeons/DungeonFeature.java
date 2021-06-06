package svenhjol.charm.module.biome_dungeons;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.EmptyBlockGetter;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.NoiseColumn;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.feature.structures.StructurePoolElement;
import net.minecraft.world.level.levelgen.feature.structures.StructureTemplatePool;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureStart;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

import java.util.Random;

public class DungeonFeature extends StructureFeature<JigsawConfiguration> {
    public static final int MIN_Y = 2;

    public DungeonFeature(Codec<JigsawConfiguration> codec) {
        super(codec);
    }

    public StructureFeature.StructureStartFactory<JigsawConfiguration> getStartFactory() {
        return (feature, chunkPos, i, l) -> new Start(this, chunkPos, i, l);
    }

    public static class Start extends NoiseAffectingStructureStart<JigsawConfiguration> {
        public Start(DungeonFeature feature, ChunkPos chunkPos, int i, long l) {
            super(feature, chunkPos, i, l);
        }

        @Override
        public void generatePieces(RegistryAccess dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, JigsawConfiguration structurePoolFeatureConfig, LevelHeightAccessor heightLimitView) {
            int x = chunkPos.getMinBlockX();
            int y = MIN_Y + new Random().nextInt(44) + 4;
            int z = chunkPos.getMinBlockZ();
            boolean found = false;
            BlockPos.MutableBlockPos blockPos = new BlockPos.MutableBlockPos(x, y, z);

            Random random = new Random(blockPos.asLong());
            Random random1 = new Random(blockPos.asLong()); // this gets passed to the generator and must be the same seed as the one used here.

            Rotation blockRotation = Rotation.getRandom(random);
            StructureTemplatePool structurePool = structurePoolFeatureConfig.startPool().get();
            StructurePoolElement structurePoolElement = structurePool.getRandomTemplate(new Random());
            BoundingBox box = structurePoolElement.getBoundingBox(structureManager, blockPos, blockRotation);

            int[] corner1 = new int[]{box.minX(), box.minZ()};
            int[] corner2 = new int[]{box.maxX(), box.minZ()};
            int[] corner3 = new int[]{box.minX(), box.maxZ()};
            int[] corner4 = new int[]{box.maxX(), box.maxZ()};

            NoiseColumn cornerSample1 = chunkGenerator.getBaseColumn(corner1[0], corner1[1], heightLimitView);
            NoiseColumn cornerSample2 = chunkGenerator.getBaseColumn(corner2[0], corner2[1], heightLimitView);
            NoiseColumn cornerSample3 = chunkGenerator.getBaseColumn(corner3[0], corner3[1], heightLimitView);
            NoiseColumn cornerSample4 = chunkGenerator.getBaseColumn(corner4[0], corner4[1], heightLimitView);

            do {
                BlockPos pos1 = new BlockPos(corner1[0], y - 1, corner1[1]);
                BlockPos pos2 = new BlockPos(corner2[0], y-1, corner2[1]);
                BlockPos pos3 = new BlockPos(corner3[0], y-1, corner3[1]);
                BlockPos pos4 = new BlockPos(corner4[0], y - 1, corner4[1]);

                BlockState state1 = cornerSample1.getBlockState(pos1);
                BlockState state2 = cornerSample2.getBlockState(pos2);
                BlockState state3 = cornerSample3.getBlockState(pos3);
                BlockState state4 = cornerSample4.getBlockState(pos4);

                if ((state1.isRedstoneConductor(EmptyBlockGetter.INSTANCE, pos1) || state1.isAir())
                    && state2.isRedstoneConductor(EmptyBlockGetter.INSTANCE, pos2)
                    && state3.isRedstoneConductor(EmptyBlockGetter.INSTANCE, pos3)
                    && state4.isRedstoneConductor(EmptyBlockGetter.INSTANCE, pos4)) {
                    found = true;
                }
            } while (--y > MIN_Y && !found);

            if (found) {
                BlockPos foundPos = new BlockPos(x, y + 1, z);
                Pools.bootstrap();
                JigsawPlacement.addPieces(dynamicRegistryManager, structurePoolFeatureConfig, PoolElementStructurePiece::new, chunkGenerator, structureManager, foundPos, this, random1, true, false, heightLimitView);
                this.getBoundingBox();
            }
        }
    }
}
