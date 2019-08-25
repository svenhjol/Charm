package svenhjol.meson;

import org.apache.logging.log4j.LogManager;

public class Meson
{
    public static final String MOD_ID = "Meson";

    public static void debug(Object... out)
    {
        for (Object obj : out) {
            LogManager.getLogger(MOD_ID).debug(obj);
        }
    }

    public static void log(Object... out)
    {
        for (Object obj : out) {
            LogManager.getLogger(MOD_ID).info(obj);
        }
    }

    public static void warn(Object... out)
    {
        for (Object obj : out) {
            LogManager.getLogger(MOD_ID).warn(obj);
        }
    }
}
