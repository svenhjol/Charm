package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;

public abstract class RegisterHolder<F extends Feature> extends FeatureHolder<F> implements ConditionalRunner {
    public RegisterHolder(F feature) {
        super(feature);
        feature.loader().registerRunner(this);
    }

    @Override
    public boolean isEnabled() {
        return feature().isEnabled();
    }
}
