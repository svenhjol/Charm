package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;

public abstract class Register<T extends Feature> {
    protected T feature;

    public Register(T feature) {
        this.feature = feature;
        log().debug("Initializing registration class " + name() + " for " + feature.name());
        feature.loader().onRegisterComplete(this);
    }

    public T feature() {
        return feature;
    }

    public Log log() {
        return this.feature.log();
    }

    public String name() {
        return this.getClass().getSimpleName();
    }

    @Deprecated
    public void onRegister() {
        // no op
    }

    public void onEnabled() {
        // no op
    }

    public void onDisabled() {
        // no op
    }
}
