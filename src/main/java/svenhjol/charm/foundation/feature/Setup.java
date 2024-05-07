package svenhjol.charm.foundation.feature;

import svenhjol.charm.foundation.Feature;
import svenhjol.charm.foundation.Log;
import svenhjol.charm.foundation.Resolve;

import java.util.function.Supplier;

public class Setup<T extends SetupRunner> {
    private T instance;
    private final Feature feature;
    private final Supplier<T> supplier;
    private final Log log;

    public Setup(Feature feature, Supplier<T> supplier) {
        this.supplier = supplier;
        this.feature = feature;
        this.log = new Log(feature.modId(), feature.name() + "/Setup");
        feature.loader().registerSetup(this);
    }

    public T get() {
        if (instance == null) {
            instance = supplier.get();
            Resolve.register(instance);
            feature.log().debug("Registered resolved setup " + instance.getClass().getSimpleName());
        }
        return instance;
    }

    public Feature feature() {
        return feature;
    }

    public Supplier<T> supplier() {
        return supplier;
    }

    public void run() {
        if (feature.isEnabled()) {
            get().onEnabled();
        } else {
            get().onDisabled();
        }
        log.debug("Ran " + get().getClass().getSimpleName());
    }

    public static <T extends SetupRunner> Setup<T> create(Feature feature, Supplier<T> supplier) {
        return new Setup<>(feature, supplier);
    }
}
