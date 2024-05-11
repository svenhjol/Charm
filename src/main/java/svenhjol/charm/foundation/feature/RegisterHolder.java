package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;

public abstract class RegisterHolder<F extends Feature> extends FeatureHolder<F> implements Conditional {
    public RegisterHolder(F feature) {
        super(feature);
        feature.loader().registerConditional(this);
    }

    @Override
    public boolean isEnabled() {
        return feature().isEnabled();
    }
}
