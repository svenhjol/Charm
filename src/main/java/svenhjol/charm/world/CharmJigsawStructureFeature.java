package svenhjol.charm.world;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.data.worldgen.Pools;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraft.world.level.levelgen.feature.configurations.JigsawConfiguration;
import net.minecraft.world.level.levelgen.feature.structures.JigsawPlacement;
import net.minecraft.world.level.levelgen.structure.NoiseAffectingStructureStart;
import net.minecraft.world.level.levelgen.structure.PoolElementStructurePiece;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureManager;

public class CharmJigsawStructureFeature extends StructureFeature<JigsawConfiguration> {
    private final int structureStartY;
    private final int variation;
    private final boolean relativeToBottom;

    public CharmJigsawStructureFeature(Codec<JigsawConfiguration> codec, int structureStartY, int variation, boolean relativeToBottom) {
        super(codec);
        this.structureStartY = structureStartY;
        this.variation = variation;
        this.relativeToBottom = relativeToBottom;
    }

    public StructureFeature.StructureStartFactory<JigsawConfiguration> getStartFactory() {
        return (feature, chunkPos, i, l) -> new Start(this, chunkPos, i, l);
    }

    public static class Start extends NoiseAffectingStructureStart<JigsawConfiguration> {
        private final CharmJigsawStructureFeature jigsawFeature;

        public Start(CharmJigsawStructureFeature feature, ChunkPos chunkPos, int i, long l) {
            super(feature, chunkPos, i, l);
            this.jigsawFeature = feature;
        }

        public void init(RegistryAccess dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, JigsawConfiguration structurePoolFeatureConfig, LevelHeightAccessor heightLimitView) {

            int v = jigsawFeature.variation;
            int r = v > 0 ? random.nextInt(v * 2) : 0;
            int y;

            if (jigsawFeature.relativeToBottom) {
                y = chunkGenerator.getMinY() + jigsawFeature.structureStartY + (v - r);
            } else {
                y = jigsawFeature.structureStartY + (v - r);
            }

            BlockPos blockPos = new BlockPos(chunkPos.getMinBlockX(), y, chunkPos.getMinBlockZ());
            Pools.bootstrap();
            JigsawPlacement.addPieces(dynamicRegistryManager, structurePoolFeatureConfig, PoolElementStructurePiece::new, chunkGenerator, structureManager, blockPos, this, random, false, false, heightLimitView);
            this.getBoundingBox();
        }
    }
}
