package svenhjol.charm.crafting.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import svenhjol.charm.crafting.feature.Composter;
import svenhjol.meson.MesonMessage;

public class MessageComposterAddLevel extends MesonMessage
{
    public BlockPos pos;
    public int level;

    public MessageComposterAddLevel(BlockPos pos, int level)
    {
        this.pos = pos;
        this.level = level;
    }

    @SuppressWarnings("unused")
    public MessageComposterAddLevel()
    {
        // no op
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        buf.writeInt(level);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
        level = buf.readInt();
    }

    @Override
    public IMessage handle(MessageContext context)
    {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            Composter.spawnComposterParticles(pos, level);
        });

        return null;
    }
}
