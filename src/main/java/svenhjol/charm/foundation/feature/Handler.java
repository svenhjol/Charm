package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Resolve;

public abstract class Handler<T extends Feature> implements SetupRunner {
    protected T resolved;

    public T feature() {
        if (resolved == null) {
            resolved = Resolve.feature(type());
        }
        return resolved;
    }

    protected abstract Class<T> type();

    public Log log() {
        return feature().log();
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

}
