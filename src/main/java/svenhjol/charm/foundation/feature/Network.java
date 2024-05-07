package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Resolve;

public abstract class Network<T extends Feature> implements SetupRunner {
    private T resolved;

    public T feature() {
        if (resolved == null) {
            resolved = Resolve.feature(featureClass());
        }
        return resolved;
    }

    protected abstract Class<T> featureClass();

    public Log log() {
        return feature().log();
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

    @Deprecated
    public void onRegister() {
        // no op
    }
}
