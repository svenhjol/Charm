package svenhjol.charm.feature.storage_blocks;

import svenhjol.charmony_api.iface.IRegistry;

import java.util.List;
import java.util.function.BooleanSupplier;

public interface IStorageBlockFeature<R extends IRegistry> {
    void preRegister(R registry);

    default void register() {}

    boolean isEnabled();

    default List<BooleanSupplier> checks() {
        return List.of(() -> true);
    }

    default void runWhenEnabled() {}

    default void runWhenDisabled() {}
}
