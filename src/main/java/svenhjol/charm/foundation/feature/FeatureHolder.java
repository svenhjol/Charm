package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;

public abstract class FeatureHolder<F extends Feature> {
    private final F feature;

    public FeatureHolder(F feature) {
        this.feature = feature;
    }

    public F feature() {
        return this.feature;
    }

    public Log log() {
        return this.feature.log();
    }
}
