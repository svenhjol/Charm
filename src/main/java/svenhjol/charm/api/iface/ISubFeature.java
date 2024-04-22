package svenhjol.charm.api.iface;

import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Sub-feature loader interface.
 */
@SuppressWarnings("unused")
public interface ISubFeature {
    <T extends IRegistry> void preRegister(T registry);

    default void register() {}

    boolean isEnabled();

    default List<BooleanSupplier> checks() {
        return List.of(() -> true);
    }

    default void runWhenEnabled() {}

    default void runWhenDisabled() {}
}
