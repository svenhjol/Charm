package svenhjol.charm.foundation.feature;

public interface ConditionalRunner {
    /**
     * Run tasks when the associated class is enabled.
     */
    default void onEnabled() {
        // no op
    }

    /**
     * Run tasks when the associated class is disabled.
     */
    default void onDisabled() {
        // no op
    }

    boolean isEnabled();
}
