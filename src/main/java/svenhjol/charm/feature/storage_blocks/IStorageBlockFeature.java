package svenhjol.charm.feature.storage_blocks;

import java.util.List;
import java.util.function.BooleanSupplier;

public interface IStorageBlockFeature {
    void register();

    default void preRegister() {}

    default List<BooleanSupplier> checks() {
        return List.of(() -> true);
    }

    default void runWhenEnabled() {}

    default void runWhenDisabled() {}
}
