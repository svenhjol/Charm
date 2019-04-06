package svenhjol.charm.world.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import svenhjol.charm.world.feature.BatBucket;
import svenhjol.meson.MesonMessage;

public class MessageGlowing extends MesonMessage
{
    public double range;
    public int length;

    public MessageGlowing(int length, double range)
    {
        this.length = length;
        this.range = range;
    }

    public MessageGlowing()
    {

    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(length);
        buf.writeDouble(range);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        length = buf.readInt();
        range = buf.readDouble();
    }

    @Override
    public IMessage handle(MessageContext context)
    {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            BatBucket.clientRange = range;
            BatBucket.clientExistingTicks = length;
        });

        return null;
    }
}
