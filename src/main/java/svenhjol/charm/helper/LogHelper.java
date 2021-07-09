package svenhjol.charm.helper;

import org.apache.logging.log4j.Logger;

/**
 * @version 1.0.0-charm
 */
public class LogHelper {
    public static Logger INSTANCE;

    public static void info(Class<?> source, String message, Object... args) {
        if (INSTANCE != null)
            INSTANCE.info(assembleMessage(source, message), args);
    }

    public static void warn(Class<?> source, String message, Object... args) {
        if (INSTANCE != null)
            INSTANCE.warn(assembleMessage(source, message), args);
    }

    public static void error(Class<?> source, String message, Object... args) {
        if (INSTANCE != null)
            INSTANCE.error(assembleMessage(source, message), args);
    }

    public static void debug(Class<?> source, String message, Object... args) {
        if (INSTANCE != null && DebugHelper.isDebugMode())
            INSTANCE.info(assembleMessage(source, message), args);
    }

    private static String assembleMessage(Class<?> source, String message) {
        return "[" + source.getSimpleName() + "] " + message;
    }
}
