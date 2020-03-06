package svenhjol.meson.handler;

import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import svenhjol.meson.MesonInstance;
import svenhjol.meson.iface.IMesonMessage;

import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class PacketHandler
{
    private int index = 0;
    private final MesonInstance instance;
    private final SimpleChannel channel;

    public PacketHandler(MesonInstance instance)
    {
        this(instance, "main", 1);
    }

    public PacketHandler(MesonInstance instance, String channelName, int protocol)
    {
        this.instance = instance;
        String s = String.valueOf(protocol);

        this.channel = NetworkRegistry.ChannelBuilder
            .named(new ResourceLocation(instance.getId(), channelName))
            .clientAcceptedVersions(s::equals)
            .serverAcceptedVersions(s::equals)
            .networkProtocolVersion(() -> s)
            .simpleChannel();
    }

    public <MSG> void register(Class<MSG> clazz, BiConsumer<MSG, PacketBuffer> encoder, Function<PacketBuffer, MSG> decoder, BiConsumer<MSG, Supplier<NetworkEvent.Context>> messageConsumer)
    {
        instance.log.debug("Registering message " + clazz + ", index " + index);
        channel.registerMessage(index, clazz, encoder, decoder, messageConsumer);
        index++;
    }

    public void sendToAll(IMesonMessage msg)
    {
        for (ServerPlayerEntity player : ServerLifecycleHooks.getCurrentServer().getPlayerList().getPlayers()) {
            sendTo(msg, player);
        }
    }

    public void sendNonLocal(IMesonMessage msg, ServerPlayerEntity player)
    {
        if (player.server.isDedicatedServer()
            || !player.getGameProfile().getName().equals(player.server.getServerOwner())
        ) {
            this.channel.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    /**
     * Send from client to server. Must be called client-side.
     * @param msg Message to send
     */
    public void sendToServer(IMesonMessage msg)
    {
        this.channel.sendToServer(msg);
    }

    /**
     * Send to specific player. Must be called server-side.
     * @param msg Message to send
     * @param player Player to send to
     */
    public void sendTo(IMesonMessage msg, ServerPlayerEntity player)
    {
        if (!(player instanceof FakePlayer))
            this.channel.sendTo(msg, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }
}
