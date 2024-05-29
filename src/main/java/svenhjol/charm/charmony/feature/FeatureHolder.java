package svenhjol.charm.charmony.feature;

import svenhjol.charm.charmony.Feature;
import svenhjol.charm.charmony.Log;

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
