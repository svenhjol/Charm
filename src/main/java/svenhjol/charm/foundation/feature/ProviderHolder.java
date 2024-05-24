package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Api;

public abstract class ProviderHolder<F extends Feature> extends FeatureHolder<F> implements Conditional {
    public ProviderHolder(F feature) {
        super(feature);
        Api.registerProvider(this);
        feature.loader().registerConditional(this);
    }

    @Override
    public boolean isEnabled() {
        return feature().isEnabled();
    }
}
