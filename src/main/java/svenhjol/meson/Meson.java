package svenhjol.meson;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Meson
{
    public static final String MOD_ID = "Meson";
    public static boolean DEBUG = true;

    public static void debug(Object... out)
    {
        if (DEBUG) {
            for (Object obj : out) {
                getLogger().debug(obj);
            }
        }
    }

    public static void log(Object... out)
    {
        for (Object obj : out) {
            getLogger().info(obj);
        }
    }

    public static void warn(Object... out)
    {
        for (Object obj : out) {
            getLogger().warn(obj);
        }
    }

    public static void error(String msg, Object... out)
    {
        for (Object obj : out) {
            getLogger().error(obj);
        }
        throw new RuntimeException(msg);
    }

    public static Logger getLogger()
    {
        return LogManager.getLogger(MOD_ID);
    }
}
