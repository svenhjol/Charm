//package svenhjol.charm.crafting.message;
//
//import io.netty.buffer.ByteBuf;
//import net.minecraft.client.Minecraft;
//import net.minecraft.client.multiplayer.WorldClient;
//import net.minecraft.init.SoundEvents;
//import net.minecraft.util.EnumParticleTypes;
//import net.minecraft.util.math.BlockPos;
//import net.minecraft.util.math.MathHelper;
//import net.minecraft.world.World;
//import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
//import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
//import svenhjol.meson.MesonMessage;
//import svenhjol.meson.helper.SoundHelper;
//
//public class MessageComposterInteract extends MesonMessage
//{
//    public BlockPos pos;
//    public int event;
//
//    public MessageComposterInteract(BlockPos pos, int event)
//    {
//        this.pos = pos;
//        this.event = event;
//    }
//
//    @SuppressWarnings("unused")
//    public MessageComposterInteract()
//    {
//        // no op
//    }
//
//    @Override
//    public void toBytes(ByteBuf buf)
//    {
//        buf.writeLong(pos.toLong());
//        buf.writeInt(event);
//    }
//
//    @Override
//    public void fromBytes(ByteBuf buf)
//    {
//        pos = BlockPos.fromLong(buf.readLong());
//        event = buf.readInt();
//    }
//
//    @Override
//    public IMessage handle(MessageContext context)
//    {
//        Minecraft.getMinecraft().addScheduledTask(() -> {
//            WorldClient world = Minecraft.getMinecraft().world;
//
//            switch (event) {
//                case 0:
//                    spawnAddLevelParticles(world, pos);
//                    break;
//
//                case 1:
//                    playAddItemSound(world, pos);
//                    break;
//
//                case 2:
//                    playSpawnOutputSound(world, pos);
//                    break;
//            }
//
//        });
//
//        return null;
//    }
//
//
//}
