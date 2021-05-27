package svenhjol.charm.world;

import com.mojang.serialization.Codec;
import net.minecraft.structure.MarginedStructureStart;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class CharmJigsawStructureFeature extends StructureFeature<StructurePoolFeatureConfig> {
    private final int structureStartY;
    private final int variation;
    private final boolean surface;

    public CharmJigsawStructureFeature(Codec<StructurePoolFeatureConfig> codec, int structureStartY, int variation, boolean surface) {
        super(codec);
        this.structureStartY = structureStartY;
        this.variation = variation;
        this.surface = surface;
    }

    public StructureFeature.StructureStartFactory<StructurePoolFeatureConfig> getStructureStartFactory() {
        return (feature, chunkPos, i, l) -> new Start(this, chunkPos, i, l);
    }

    public static class Start extends MarginedStructureStart<StructurePoolFeatureConfig> {
        private final CharmJigsawStructureFeature jigsawFeature;

        public Start(CharmJigsawStructureFeature feature, ChunkPos chunkPos, int i, long l) {
            super(feature, chunkPos, i, l);
            this.jigsawFeature = feature;
        }

        public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, StructurePoolFeatureConfig structurePoolFeatureConfig, HeightLimitView heightLimitView) {

            int v = jigsawFeature.variation;
            int r = random.nextInt(v * 2);
            int y = jigsawFeature.structureStartY + (v - r);

            BlockPos blockPos = new BlockPos(chunkPos.getStartX(), y, chunkPos.getStartZ());
            StructurePools.initDefaultPools();
            StructurePoolBasedGenerator.method_30419(dynamicRegistryManager, structurePoolFeatureConfig, PoolStructurePiece::new, chunkGenerator, structureManager, blockPos, this, this.random, false, this.jigsawFeature.surface, heightLimitView);
            this.setBoundingBoxFromChildren();
        }
    }
}
