package svenhjol.charm.base.message;

import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import svenhjol.charm.tools.module.BatInABucket;
import svenhjol.meson.iface.IMesonMessage;

import java.util.function.Supplier;

public class MessageGlowing implements IMesonMessage
{
    private double range;
    private int ticks;

    public MessageGlowing(double range, int ticks)
    {
        this.range = range;
        this.ticks = ticks;
    }

    public static void encode(MessageGlowing msg, PacketBuffer buf)
    {
        buf.writeDouble(msg.range);
        buf.writeInt(msg.ticks);
    }

    public static MessageGlowing decode(PacketBuffer buf)
    {
        return new MessageGlowing(buf.readDouble(), buf.readInt());
    }

    public static class Handler
    {
        public static void handle(final MessageGlowing msg, Supplier<NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> {
                BatInABucket.clientRange = msg.range;
                BatInABucket.clientTicks = msg.ticks;
            });
            ctx.get().setPacketHandled(true);
        }
    }
}
