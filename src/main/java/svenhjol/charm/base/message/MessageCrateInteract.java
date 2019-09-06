package svenhjol.charm.base.message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import svenhjol.charm.decoration.module.Crates;
import svenhjol.meson.iface.IMesonMessage;

import java.util.function.Supplier;

public class MessageCrateInteract implements IMesonMessage
{
    private BlockPos pos;
    private int type;

    public MessageCrateInteract(BlockPos pos, int type)
    {
        this.pos = pos;
        this.type = type;
    }

    public static void encode(MessageCrateInteract msg, PacketBuffer buf)
    {
        buf.writeLong(msg.pos.toLong());
        buf.writeInt(msg.type);
    }

    public static MessageCrateInteract decode(PacketBuffer buf)
    {
        return new MessageCrateInteract(BlockPos.fromLong(buf.readLong()), buf.readInt());
    }

    public static class Handler
    {
        public static void handle(final MessageCrateInteract msg, Supplier <NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> Crates.effectInteract(msg.pos, msg.type));
            ctx.get().setPacketHandled(true);
        }
    }
}
