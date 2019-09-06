package svenhjol.charm.base.message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import svenhjol.charm.enchanting.module.Magnetic;
import svenhjol.meson.iface.IMesonMessage;

import java.util.function.Supplier;

public class MessageMagneticPickup implements IMesonMessage
{
    private BlockPos pos;

    public MessageMagneticPickup(BlockPos pos)
    {
        this.pos = pos;
    }

    public static void encode(MessageMagneticPickup msg, PacketBuffer buf)
    {
        buf.writeLong(msg.pos.toLong());
    }

    public static MessageMagneticPickup decode(PacketBuffer buf)
    {
        return new MessageMagneticPickup(BlockPos.fromLong(buf.readLong()));
    }

    public static class Handler
    {
        public static void handle(final MessageMagneticPickup msg, Supplier <NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> Magnetic.effectPickup(msg.pos));
            ctx.get().setPacketHandled(true);
        }
    }
}
