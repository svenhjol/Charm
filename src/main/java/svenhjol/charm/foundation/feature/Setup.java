package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Resolve;

import java.util.function.Supplier;

public class Setup<S extends SetupRunner<?>> {
    private final Feature feature;
    private final Supplier<S> supplier;
    private final Log log;
    private S instance;
    private boolean hasRun = false;

    private Setup(Feature feature, Supplier<S> supplier) {
        this.supplier = supplier;
        this.feature = feature;
        this.log = new Log(feature.modId(), feature.name() + "/Setup");
        feature.loader().registerSetup(this);
    }

    /**
     * Gets a resolved instance from the setup supplier.
     * Use this in a feature to access the feature's registered objects, handlers, advancements etc.
     */
    public S get() {
        if (instance == null) {
            instance = supplier.get();
            Resolve.register(instance);
            feature.log().debug("Registered resolved setup " + instance.getClass().getSimpleName());
        }
        return instance;
    }

    /**
     * The feature that this setup supplier belongs to.
     */
    public Feature feature() {
        return feature;
    }

    /**
     * The supplier of the setup instance.
     */
    public Supplier<S> supplier() {
        return supplier;
    }

    /**
     * Called by the mod loader's run() step.
     */
    public void run() {
        if (hasRun) return;

        if (feature.isEnabled()) {
            get().onEnabled();
        } else {
            get().onDisabled();
        }

        log.debug("Ran " + get().getClass().getSimpleName());
        hasRun = true;
    }

    /**
     * Create a new setup supplier.
     * @param feature Reference to the feature that the setup class belongs to.
     * @param supplier The supplier of the setup instance.
     * @return Resolved setup instance.
     * @param <T> Setup class
     */
    public static <T extends SetupRunner<?>> Setup<T> create(Feature feature, Supplier<T> supplier) {
        return new Setup<>(feature, supplier);
    }
}
