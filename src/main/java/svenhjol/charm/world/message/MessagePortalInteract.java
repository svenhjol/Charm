package svenhjol.charm.world.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import svenhjol.charm.world.feature.EndPortalRunes;
import svenhjol.meson.MesonMessage;

public class MessagePortalInteract extends MesonMessage
{
    public BlockPos pos;
    public int linkType;

    public MessagePortalInteract(BlockPos pos, int linkType)
    {
        this.pos = pos;
        this.linkType = linkType;
    }

    @SuppressWarnings("unused")
    public MessagePortalInteract()
    {
        // no op
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        buf.writeInt(linkType);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
        linkType = buf.readInt();
    }

    @Override
    public IMessage handle(MessageContext context)
    {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            switch(linkType) {
                case 0:
                    EndPortalRunes.effectPortalUnlinked(pos);
                    break;
                case 1:
                    EndPortalRunes.effectPortalLinked(pos);
                    break;
                case 2:
                    EndPortalRunes.effectPortalTravelled(pos);
            }
        });

        return null;
    }
}
