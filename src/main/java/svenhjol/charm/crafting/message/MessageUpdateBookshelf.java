package svenhjol.charm.crafting.message;

import io.netty.buffer.ByteBuf;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import svenhjol.charm.crafting.block.BlockBookshelfChest;
import svenhjol.meson.MesonMessage;

public class MessageUpdateBookshelf extends MesonMessage
{
    public BlockPos pos;
    public int books;

    @SuppressWarnings("unused")
    public MessageUpdateBookshelf(BlockPos pos, int books)
    {
        this.pos = pos;
        this.books = books;
    }

    @SuppressWarnings("unused")
    public MessageUpdateBookshelf()
    {
        // no op
    }

    @Override
    public void toBytes(ByteBuf buf)
    {
        buf.writeLong(pos.toLong());
        buf.writeInt(books);
    }

    @Override
    public void fromBytes(ByteBuf buf)
    {
        pos = BlockPos.fromLong(buf.readLong());
        books = buf.readInt();
    }

    @Override
    public IMessage handle(MessageContext context)
    {
        Minecraft.getMinecraft().addScheduledTask(() -> {
            WorldClient world = Minecraft.getMinecraft().world;
            IBlockState state = world.getBlockState(pos);
            world.setBlockState(pos, state.withProperty(BlockBookshelfChest.SLOTS, books), 2);
        });

        return null;
    }
}
