package svenhjol.charm.foundation.helper;

import java.util.function.Supplier;

public final class EnumHelper {
    public static <T> T getValueOrDefault(Supplier<T> valueOf, T defaultValue) {
        try {
            return valueOf.get();
        } catch (Exception e) {
            return defaultValue;
        }
    }
}
