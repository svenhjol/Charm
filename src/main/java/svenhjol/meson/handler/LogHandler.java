package svenhjol.meson.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import svenhjol.charm.base.module.Debug;

public class LogHandler {
    public String name;
    public Marker marker = MarkerManager.getMarker("INTERNAL");

    public LogHandler(String name) {
        this.name = name;
    }

    public void debug(String msg) {
        debug(marker, msg);
    }

    public void debug(Marker marker, String msg) {
        if (Debug.showDebugMessages) {
            info(marker, msg);
        } else {
            getLogger().debug(marker, msg);
        }
    }

    public void info(String msg) {
        info(marker, msg);
    }

    public void info(Marker marker, String msg) {
        getLogger().info(marker, msg);
    }

    public void warn(String msg) {
        warn(marker, msg);
    }

    public void warn(Marker marker, String msg) {
        getLogger().warn(marker, msg);
    }

    public void error(String msg) {
        error(marker, msg);
    }

    public void error(Marker marker, String msg) {
        getLogger().error(marker, msg);
    }

    public Logger getLogger() {
        return LogManager.getLogger(this.name);
    }
}
