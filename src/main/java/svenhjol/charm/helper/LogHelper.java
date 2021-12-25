package svenhjol.charm.helper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * @version 4.0.0-charm
 */
public class LogHelper {
    public static String DEFAULT_INSTANCE;
    public static Map<String, Logger> INSTANCES = new HashMap<>();

    public static Logger instance(String modId) {
        return INSTANCES.computeIfAbsent(modId, m -> LogManager.getFormatterLogger(StringHelper.capitalize(modId)));
    }

    public static void info(Class<?> source, String message, Object... args) {
        if (DEFAULT_INSTANCE != null) {
            info(DEFAULT_INSTANCE, source, message, args);
        }
    }

    public static void info(String modId, Class<?> source, String message, Object... args) {
        instance(modId).info(assembleMessage(source, message), args);
    }

    public static void warn(Class<?> source, String message, Object... args) {
        if (DEFAULT_INSTANCE != null) {
            warn(DEFAULT_INSTANCE, source, message, args);
        }
    }

    public static void warn(String modId, Class<?> source, String message, Object... args) {
        instance(modId).warn(assembleMessage(source, message), args);
    }

    public static void error(Class<?> source, String message, Object... args) {
        if (DEFAULT_INSTANCE != null) {
            error(DEFAULT_INSTANCE, source, message, args);
        }
    }

    public static void error(String modId, Class<?> source, String message, Object... args) {
        instance(modId).error(assembleMessage(source, message), args);
    }

    public static void debug(Class<?> source, String message, Object... args) {
        if (DEFAULT_INSTANCE != null) {
            debug(DEFAULT_INSTANCE, source, message, args);
        }
    }

    public static void debug(String modId, Class<?> source, String message, Object... args) {
        if (DebugHelper.isDebugMode()) {
            instance(modId).info(assembleMessage(source, message), args);
        }
    }

    private static String assembleMessage(Class<?> source, String message) {
        return "[" + source.getSimpleName() + "] " + message;
    }
}
