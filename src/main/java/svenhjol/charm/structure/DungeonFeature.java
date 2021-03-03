package svenhjol.charm.structure;

import com.mojang.serialization.Codec;
import net.minecraft.world.gen.feature.JigsawFeature;
import net.minecraft.world.gen.feature.StructurePoolFeatureConfig;

public class DungeonFeature extends JigsawFeature {
    public DungeonFeature(Codec<StructurePoolFeatureConfig> codec) {
        super(codec, 24, false, false);
    }
}
