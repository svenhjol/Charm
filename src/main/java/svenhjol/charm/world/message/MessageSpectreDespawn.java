package svenhjol.charm.world.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import svenhjol.charm.world.feature.Spectre;
import svenhjol.meson.MesonMessage;

public class MessageSpectreDespawn extends MesonMessage
{
    public BlockPos pos;

    public MessageSpectreDespawn(BlockPos pos)
    {
        this.pos = pos;
    }

    public MessageSpectreDespawn()
    {
        // no op
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
    }

    @Override
    public IMessage handle(MessageContext context)
    {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            Spectre.effectDespawn(pos);
        });

        return null;
    }
}
