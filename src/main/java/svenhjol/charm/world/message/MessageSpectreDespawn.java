package svenhjol.charm.world.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import svenhjol.meson.MesonMessage;

public class MessageSpectreDespawn extends MesonMessage
{
    public int dimension;
    public BlockPos pos;

    public MessageSpectreDespawn(int dimension, BlockPos pos)
    {
        this.dimension = dimension;
        this.pos = pos;
    }

    public MessageSpectreDespawn()
    {
        // no op
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeInt(dimension);
        buf.writeLong(pos.toLong());
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        dimension = buf.readInt();
        pos = BlockPos.fromLong(buf.readLong());
    }
}
