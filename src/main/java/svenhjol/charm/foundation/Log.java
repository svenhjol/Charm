package svenhjol.charm.foundation;

import com.google.common.base.CaseFormat;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import svenhjol.charm.foundation.helper.ConfigHelper;

public class Log {
    private final Logger log;

    public Log(String id) {
        log = LogManager.getFormatterLogger(snakeToUpperCamel(id));
    }

    public Log(String id, String suffix) {
        var name = snakeToUpperCamel(id) + "/" + suffix;
        log = LogManager.getFormatterLogger(name);
    }

    public Log(String id, Object object) {
        this(id, object.getClass().getSimpleName());
    }

    public void info(String message, Object... args) {
        log.info(message, args);
    }

    public void warn(String message, Object... args) {
        log.warn(message, args);
    }

    public void error(String message, Object... args) {
        log.error(message, args);
    }

    public void debug(String message, Object... args) {
        if (ConfigHelper.isDebugEnabled()) {
            info(message, args);
        }
    }

    private String snakeToUpperCamel(String string) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string);
    }
}
