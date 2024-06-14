package svenhjol.charm.charmony.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BooleanSupplier;

public final class DebugHelper {
    private final static Logger LOGGER = LogManager.getLogger("DebugHelper");
    private static final List<BooleanSupplier> DEBUG_CHECKS = new ArrayList<>();
    private static final List<BooleanSupplier> MIXIN_DISABLE_CHECKS = new ArrayList<>();

    public static void registerDebugCheck(BooleanSupplier supplier) {
        LOGGER.info("Adding debug check: " + supplier);
        DEBUG_CHECKS.add(supplier);
    }

    public static void registerMixinDisableCheck(BooleanSupplier supplier) {
        LOGGER.info("Adding mixin disable check: " + supplier);
        MIXIN_DISABLE_CHECKS.add(supplier);
    }

    public static boolean isDebugEnabled() {
        return DEBUG_CHECKS.stream().anyMatch(BooleanSupplier::getAsBoolean);
    }

    public static boolean isMixinDisableModeEnabled() {
        return MIXIN_DISABLE_CHECKS.stream().anyMatch(BooleanSupplier::getAsBoolean);
    }
}
