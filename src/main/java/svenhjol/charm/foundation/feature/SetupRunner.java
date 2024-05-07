package svenhjol.charm.foundation.feature;

public interface SetupRunner {
    default void onEnabled() {
        // no op
    }

    default void onDisabled() {
        // no op
    }

    default String name() {
        return this.getClass().getSimpleName();
    }
}
