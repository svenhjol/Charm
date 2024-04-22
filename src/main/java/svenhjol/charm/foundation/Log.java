package svenhjol.charm.foundation;

import com.google.common.base.CaseFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import svenhjol.charm.foundation.helper.ConfigHelper;

public class Log {
    private final Logger log;

    public Log(String namespace) {
        log = LogManager.getFormatterLogger(snakeToUpperCamel(namespace));
    }

    public void info(Class<?> source, String message, Object... args) {
        log.info(message(source, message), args);
    }

    public void warn(Class<?> source, String message, Object... args) {
        log.warn(message(source, message), args);
    }

    public void error(Class<?> source, String message, Object... args) {
        log.error(message(source, message), args);
    }

    public void debug(Class<?> source, String message, Object... args) {
        if (ConfigHelper.isDebugEnabled()) {
            info(source, message, args);
        }
    }

    private String message(Class<?> source, String message) {
        return "[" + source.getSimpleName() + "] " + message;
    }

    private String snakeToUpperCamel(String string) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string);
    }
}
