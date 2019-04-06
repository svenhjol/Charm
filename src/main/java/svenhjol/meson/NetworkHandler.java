package svenhjol.meson;

import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler
{
    public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Meson.MOD_ID);

    public static int i = 0;

    public static void register(Class message, Side side)
    {
        INSTANCE.registerMessage(message, message, i++, side);
    }
}