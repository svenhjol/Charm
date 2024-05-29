package svenhjol.charm.charmony;

import com.google.common.base.CaseFormat;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import svenhjol.charm.charmony.common.helper.DebugHelper;
import svenhjol.charm.charmony.helper.ConfigHelper;

@SuppressWarnings("unused")
public final class Log {
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
        if (DebugHelper.isDebugEnabled()) {
            info("[Debug] " + message, args);
        }
    }

    public void warnIfDebug(String message, Object... args) {
        if (DebugHelper.isDebugEnabled()) {
            warn("[Debug] " + message, args);
        }
    }

    public void dev(String message, Object... args) {
        if (ConfigHelper.isDevEnvironment()) {
            info("[Dev] " + message, args);
        }
    }

    public void warnIfDev(String message, Object... args) {
        if (ConfigHelper.isDevEnvironment()) {
            warn("[Dev] " + message, args);
        }
    }

    public void die(Throwable e) throws RuntimeException {
        var cause = e.getCause();
        var message = (cause != null ? cause.getMessage() : e.getMessage());
        die(message, e);
    }

    public void die(String message, Throwable e) throws RuntimeException {
        var stacktrace = ExceptionUtils.getStackTrace(e);
        error(stacktrace);
        die(message);
    }

    public void die(String message) throws RuntimeException {
        error(message);
        throw new RuntimeException(message);
    }

    private String snakeToUpperCamel(String string) {
        return CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.UPPER_CAMEL, string);
    }
}
