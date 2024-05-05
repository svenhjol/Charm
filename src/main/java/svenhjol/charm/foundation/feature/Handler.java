package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;

public abstract class Handler<T extends Feature> {
    protected T feature;

    public Handler(T feature) {
        this.feature = feature;
        log().debug("Initializing handler class " + name() + " for " + feature.name());
    }

    public Log log() {
        return this.feature.log();
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

}
