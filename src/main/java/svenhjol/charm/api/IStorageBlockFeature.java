package svenhjol.charm.api;

import java.util.List;
import java.util.function.BooleanSupplier;

public interface IStorageBlockFeature {
    default void register() {}

    boolean isEnabled();

    default void preRegister() {}

    default List<BooleanSupplier> checks() {
        return List.of(() -> true);
    }

    default void runWhenEnabled() {}

    default void runWhenDisabled() {}
}
