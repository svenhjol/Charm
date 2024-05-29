package svenhjol.charm.charmony.common.item;

import net.minecraft.world.item.Item;
import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.feature.FeatureResolver;

public abstract class CharmItem<F extends Feature> extends Item implements FeatureResolver<F> {
    public CharmItem(Properties properties) {
        super(properties);
    }
}
