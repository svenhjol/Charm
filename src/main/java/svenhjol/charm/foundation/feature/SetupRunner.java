package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Resolve;

public abstract class SetupRunner<F extends Feature> {
    private F resolved;

    /**
     * A resolved reference to the feature that this setup belongs to.
     * @return Feature
     */
    public F feature() {
        if (resolved == null) {
            resolved = Resolve.feature(type());
        }
        return resolved;
    }

    /**
     * The setup class type.
     * @return Class type to provide IDE completion.
     */
    protected abstract Class<F> type();

    public Log log() {
        return feature().log();
    }

    /**
     * Run tasks when the associated feature is enabled.
     */
    public void onEnabled() {
        // no op
    }

    /**
     * Run tasks when the associated feature is disabled.
     */
    public void onDisabled() {
        // no op
    }

    /**
     * Helper to get the short name of the object.
     */
    public String name() {
        return this.getClass().getSimpleName();
    }
}
