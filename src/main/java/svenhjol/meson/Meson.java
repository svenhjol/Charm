package svenhjol.meson;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.FMLCommonHandler;

public class Meson
{
    public static final String MOD_ID = "Meson";
    public static boolean DEBUG = false;
    private static boolean hasInit = false;

    public static void log(Object ...out)
    {
        System.out.print("[Meson] ");
        int i = 0;
        for (Object obj : out) {
            System.out.print(obj);
            if (++i < out.length) System.out.print(", ");
        }
        System.out.println();
    }

    public static void debug(Object ...out)
    {
        System.out.println("=============== MESON DEBUG ================");
        for (Object obj : out) {
            System.out.println(obj);
        }
        System.out.println("============================================");
    }

    public static void fatal(Object ...out)
    {
        System.out.println("============ MESON DIED (✖╭╮✖) =============");
        for (Object obj : out) {
            System.out.println(obj);
        }
        System.out.println("============================================");
        exit();
    }

    public static void exit(Object ...out)
    {
        if (out != null) {
            log(out);
        }
        FMLCommonHandler.instance().exitJava(0, false);
    }

    public static void runtimeException(String message)
    {
        throw new RuntimeException("MESON DIED (✖╭╮✖): " + message);
    }

    public static void init()
    {
        if (!hasInit) {
            MinecraftForge.EVENT_BUS.register(ProxyRegistry.class);
            hasInit = true;
        }
    }
}