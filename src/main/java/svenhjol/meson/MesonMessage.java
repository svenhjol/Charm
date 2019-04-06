package svenhjol.meson;

import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public abstract class MesonMessage<REQ extends MesonMessage> implements IMessage, IMessageHandler<REQ, IMessage>
{
    public IMessage handle(MessageContext context)
    {
        return null;
    }

    @Override
    public IMessage onMessage(REQ message, MessageContext context)
    {
        return message.handle(context);
    }
}
