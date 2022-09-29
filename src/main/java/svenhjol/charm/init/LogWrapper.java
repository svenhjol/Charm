package svenhjol.charm.init;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import svenhjol.charm.helper.DebugHelper;
import svenhjol.charm.helper.StringHelper;

public class LogWrapper {
    private final Logger LOG;
    public LogWrapper(String modId) {
        LOG = LogManager.getFormatterLogger(StringHelper.capitalize(modId));
    }

    public void info(String modId, Class<?> source, String message, Object... args) {
        LOG.info(assembleMessage(source, message), args);
    }

    public void warn(String modId, Class<?> source, String message, Object... args) {
        LOG.warn(assembleMessage(source, message), args);
    }

    public void error(String modId, Class<?> source, String message, Object... args) {
        LOG.error(assembleMessage(source, message), args);
    }

    public void debug(String modId, Class<?> source, String message, Object... args) {
        if (DebugHelper.isDebugMode()) {
            LOG.info(assembleMessage(source, message), args);
        }
    }

    private String assembleMessage(Class<?> source, String message) {
        return "[" + source.getSimpleName() + "] " + message;
    }
}
