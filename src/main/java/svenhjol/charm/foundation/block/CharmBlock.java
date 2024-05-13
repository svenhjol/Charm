package svenhjol.charm.foundation.block;

import net.minecraft.world.level.block.Block;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.FeatureResolver;

public abstract class CharmBlock<F extends Feature> extends Block implements FeatureResolver<F> {
    public CharmBlock(Properties properties) {
        super(properties);
    }
}