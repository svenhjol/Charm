package svenhjol.meson.handler;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import svenhjol.meson.iface.IMesonMessage;

public class PacketHandler
{
    private static final String PROTOCOL_VERSION = Integer.toString(1);
    public static final SimpleChannel HANDLER = NetworkRegistry.ChannelBuilder
        .named(new ResourceLocation("mainchannel"))
        .clientAcceptedVersions(PROTOCOL_VERSION::equals)
        .serverAcceptedVersions(PROTOCOL_VERSION::equals)
        .networkProtocolVersion(() -> PROTOCOL_VERSION)
        .simpleChannel();


    public static int index = 0;

    public static void sendToAll(IMesonMessage msg)
    {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendTo(msg, player);
        }
    }

    public static void sendNonLocal(IMesonMessage msg, ServerPlayerEntity player)
    {
        if (player.server.isDedicatedServer()
            || !player.getGameProfile().getName().equals(player.server.getServerOwner())
        ) {
            HANDLER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    /**
     * Send from client to server. Must be called client-side.
     * @param msg Message to send
     */
    public static void sendToServer(IMesonMessage msg)
    {
        HANDLER.sendToServer(msg);
    }

    /**
     * Send to specific player. Must be called server-side.
     * @param msg Message to send
     * @param player Player to send to
     */
    public static void sendTo(IMesonMessage msg, ServerPlayerEntity player)
    {
        if (!(player instanceof FakePlayer)) {
            HANDLER.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }
}
