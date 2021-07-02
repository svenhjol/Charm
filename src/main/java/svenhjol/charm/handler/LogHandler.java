package svenhjol.charm.handler;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import svenhjol.charm.module.core.Core;

public class LogHandler {
    public String name;
    public static final Marker INTERNAL = MarkerManager.getMarker("Charm");
    public static final Marker RECIPE_HELPER = MarkerManager.getMarker("RecipeHelper");

    public LogHandler(String name) {
        this.name = name;
    }

    public void debug(String msg) {
        debug(INTERNAL, msg);
    }

    public void debug(Marker marker, String msg) {
        if (Core.isDebugMode()) {
            info(marker, msg);
        } else {
            getLogger().debug(marker, msg);
        }
    }

    public void info(String msg) {
        info(INTERNAL, msg);
    }

    public void info(Marker marker, String msg) {
        getLogger().info(marker, msg);
    }

    public void warn(String msg) {
        warn(INTERNAL, msg);
    }

    public void warn(Marker marker, String msg) {
        getLogger().warn(marker, msg);
    }

    public void error(String msg) {
        error(INTERNAL, msg);
    }

    public void error(Marker marker, String msg) {
        getLogger().error(marker, msg);
    }

    public Logger getLogger() {
        return LogManager.getLogger(this.name);
    }
}