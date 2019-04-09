package svenhjol.charm.brewing.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import svenhjol.meson.ClientHandler;
import svenhjol.meson.MesonMessage;

public class MessageSetStructure extends MesonMessage
{
    private BlockPos pos;
    private String name;

    public MessageSetStructure(String name, BlockPos pos)
    {
        this.pos = pos;
        this.name = name;
    }

    @SuppressWarnings("unused")
    public MessageSetStructure()
    {
        // no op
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
        name = ByteBufUtils.readUTF8String(buf);
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        ByteBufUtils.writeUTF8String(buf, name);
    }

    @Override
    public IMessage handle(MessageContext context)
    {
        Minecraft.getMinecraft().addScheduledTask(() -> ClientHandler.setNearestStructure(name, pos));
        return null;
    }
}
