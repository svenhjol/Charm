package svenhjol.charm.brewing.message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import svenhjol.charm.brewing.feature.FlavoredCake;
import svenhjol.meson.iface.IMesonMessage;

import java.util.function.Supplier;

public class MessageCakeImbue implements IMesonMessage
{
    private BlockPos pos;

    public MessageCakeImbue(BlockPos pos)
    {
        this.pos = pos;
    }

    public static void encode(MessageCakeImbue msg, PacketBuffer buf)
    {
        buf.writeLong(msg.pos.toLong());
    }

    public static MessageCakeImbue decode(PacketBuffer buf)
    {
        return new MessageCakeImbue(BlockPos.fromLong(buf.readLong()));
    }

    public static class Handler
    {
        public static void handle(final MessageCakeImbue msg, Supplier <NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> FlavoredCake.effectImbued(msg.pos));
            ctx.get().setPacketHandled(true);
        }
    }
}
