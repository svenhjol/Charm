package svenhjol.charm.charmony.feature;

import svenhjol.charm.charmony.Api;
import svenhjol.charm.charmony.Feature;

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
