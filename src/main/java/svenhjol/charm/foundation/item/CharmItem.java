package svenhjol.charm.foundation.item;

import net.minecraft.world.item.Item;
import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.feature.FeatureResolver;

public abstract class CharmItem<F extends Feature> extends Item implements FeatureResolver<F> {
    public CharmItem(Properties properties) {
        super(properties);
    }
}
