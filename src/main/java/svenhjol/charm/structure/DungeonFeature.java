package svenhjol.charm.structure;

import com.mojang.serialization.Codec;
import net.minecraft.structure.MarginedStructureStart;
import net.minecraft.structure.PoolStructurePiece;
import net.minecraft.structure.StructureManager;
import net.minecraft.structure.pool.StructurePoolBasedGenerator;
import net.minecraft.structure.pool.StructurePools;
import net.minecraft.util.math.BlockBox;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.HeightLimitView;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.feature.StructureFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class DungeonFeature extends StructureFeature<StructurePoolFeatureConfig> {
    public static final int DEFAULT_Y = 12;

    public DungeonFeature(Codec<StructurePoolFeatureConfig> codec) {
        super(codec);
    }

    public StructureFeature.StructureStartFactory<StructurePoolFeatureConfig> getStructureStartFactory() {
        return (feature, chunkPos, blockBox, i, l) -> new Start(this, chunkPos, blockBox, i, l);
    }

    public static class Start extends MarginedStructureStart<StructurePoolFeatureConfig> {
        public Start(DungeonFeature feature, ChunkPos chunkPos, BlockBox blockBox, int i, long l) {
            super(feature, chunkPos, blockBox, i, l);
        }

        public void init(DynamicRegistryManager dynamicRegistryManager, ChunkGenerator chunkGenerator, StructureManager structureManager, ChunkPos chunkPos, Biome biome, StructurePoolFeatureConfig structurePoolFeatureConfig, HeightLimitView heightLimitView) {
            int y = DEFAULT_Y + random.nextInt(24);
            BlockPos blockPos = new BlockPos(chunkPos.getStartX(), y, chunkPos.getStartZ());
            StructurePools.initDefaultPools();
            StructurePoolBasedGenerator.method_30419(dynamicRegistryManager, structurePoolFeatureConfig, PoolStructurePiece::new, chunkGenerator, structureManager, blockPos, this.children, this.random, false, false, heightLimitView);
            this.setBoundingBoxFromChildren();
        }
    }
}
