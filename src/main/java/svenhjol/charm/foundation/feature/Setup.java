package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;

public abstract class Setup<T extends Feature> {
    protected T feature;

    public Setup(T feature) {
        this.feature = feature;
        log().debug("Initializing setup class " + name() + " for " + feature.name());
    }

    public Log log() {
        return this.feature.log();
    }

    public String name() {
        return this.getClass().getSimpleName();
    }
}
