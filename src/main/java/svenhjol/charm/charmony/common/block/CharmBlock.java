package svenhjol.charm.charmony.common.block;

import net.minecraft.world.level.block.Block;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.feature.FeatureResolver;

public abstract class CharmBlock<F extends Feature> extends Block implements FeatureResolver<F> {
    public CharmBlock(Properties properties) {
        super(properties);
    }
}