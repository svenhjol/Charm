package svenhjol.charm.charmony.feature;

import svenhjol.charm.api.CharmApi;
import svenhjol.charm.charmony.Feature;

public abstract class ProviderHolder<F extends Feature> extends FeatureHolder<F> implements Conditional {
    public ProviderHolder(F feature) {
        super(feature);
        CharmApi.registerProvider(this);
        feature.loader().registerConditional(this);
    }

    @Override
    public boolean isEnabled() {
        return feature().isEnabled();
    }
}
