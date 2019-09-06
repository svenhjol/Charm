package svenhjol.charm.base.message;

import net.minecraft.network.PacketBuffer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import svenhjol.charm.decoration.module.BookshelfChests;
import svenhjol.meson.iface.IMesonMessage;

import java.util.function.Supplier;

public class MessageBookshelfInteract implements IMesonMessage
{
    private BlockPos pos;
    private int type;

    public MessageBookshelfInteract(BlockPos pos, int type)
    {
        this.pos = pos;
        this.type = type;
    }

    public static void encode(MessageBookshelfInteract msg, PacketBuffer buf)
    {
        buf.writeLong(msg.pos.toLong());
        buf.writeInt(msg.type);
    }

    public static MessageBookshelfInteract decode(PacketBuffer buf)
    {
        return new MessageBookshelfInteract(BlockPos.fromLong(buf.readLong()), buf.readInt());
    }

    public static class Handler
    {
        public static void handle(final MessageBookshelfInteract msg, Supplier <NetworkEvent.Context> ctx)
        {
            ctx.get().enqueueWork(() -> BookshelfChests.effectInteract(msg.pos, msg.type));
            ctx.get().setPacketHandled(true);
        }
    }
}
