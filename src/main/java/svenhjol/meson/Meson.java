package svenhjol.meson;

import net.minecraftforge.common.MinecraftForge;
import org.apache.logging.log4j.LogManager;
import svenhjol.meson.registry.ProxyRegistry;

public class Meson
{
    public static final String MOD_ID = "Meson";
    private static boolean hasInit = false;

    public static void log(Object... out)
    {
        for (Object obj : out) {
            LogManager.getLogger(MOD_ID).info(obj);
        }
    }

    public static void init()
    {
        if (!hasInit) {
            MinecraftForge.EVENT_BUS.register(ProxyRegistry.class);
            hasInit = true;
        }
    }
}
